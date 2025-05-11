<template>
  <div>

    <input
        v-model="searchQuery"
        @input="onSearchInput"
        placeholder="Поиск товаров..."
        class="search-input"
    />

    <div v-if="showError" class="error-popup">
      {{ errorMessage }}
    </div>

    <button class="add-product-btn" @click="openAddProductModal">
      ➕ Добавить товар
    </button>

    <div class="product-list">
      <div v-for="product in products"
           :key="product.id"
           class="product-card"
           @click="openProductModal(product)">
        <div class="product-image-container">
          <img v-if="product.image"
               :src="`data:image/jpeg;base64,${product.image}`"
               alt="product.name"
               class="product-image"
          />
          <div v-else class="placeholder-image">
            <svg xmlns="http://www.w3.org/2000/svg"
                 width="48" height="48" viewBox="0 0 24 24"
                 fill="none" stroke="#888" stroke-width="2"
                 stroke-linecap="round" stroke-linejoin="round">
              <rect x="3" y="3" width="18" height="18" rx="2" ry="2" />
              <circle cx="8.5" cy="8.5" r="1.5" />
              <path d="M21 15l-5-5L5 21" />
            </svg>
          </div>
        </div>

        <h3>{{ product.name }}</h3>
        <p class="price">{{ product.price }} ₽</p>

        <div v-if="cart[product.id]">
          <div class="quantity-control">
            <button @click.stop="decreaseQuantity(product.id)">-</button>
            <span>{{ cart[product.id] }}</span>
            <button @click.stop="increaseQuantity(product.id)">+</button>
          </div>
          <button class="remove-btn" @click.stop="removeFromCart(product.id)">
            Удалить из корзины
          </button>
        </div>
        <div v-else>
          <button class="add-btn" @click.stop="addToCart(product.id)">
            Добавить в корзину
          </button>
        </div>
      </div>
    </div>

    <div class="pagination">
      <button :disabled="page === 1" @click="prevPage">Назад</button>
      <span>Страница {{ totalPages === 0 ? 0 : page }} из {{ totalPages }}</span>
      <button :disabled="page === totalPages" @click="nextPage">Вперёд</button>
    </div>
  </div>

  <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
    <div class="modal">
      <h2>{{ selectedProduct.name }}</h2>
      <img v-if="selectedProduct.image"
           :src="`data:image/jpeg;base64,${selectedProduct.image}`"
           alt="selectedProduct.name"
           class="product-image"
      />
      <p><strong>Цена:</strong> {{ selectedProduct.price }} ₽</p>
      <p><strong>Описание:</strong> {{ selectedProduct.description || 'Описание отсутствует.' }}</p>
      <div v-if="cart[selectedProduct.id]">
        <div class="quantity-control">
          <button @click.stop="decreaseQuantity(selectedProduct.id)">-</button>
          <span>{{ cart[selectedProduct.id] }}</span>
          <button @click.stop="increaseQuantity(selectedProduct.id)">+</button>
        </div>
        <button class="remove-btn" @click.stop="removeFromCart(selectedProduct.id)">
          Удалить из корзины
        </button>
      </div>
      <div v-else>
        <button class="add-btn" @click.stop="addToCart(selectedProduct.id)">
          Добавить в корзину
        </button>
      </div>
      <button @click="closeModal" class="close-btn">Закрыть</button>
    </div>
  </div>
  <div v-if="showAddModal" class="modal-overlay" @click.self="closeAddProductModal">
    <div class="modal">
      <h2>Добавление товара</h2>

      <input v-model="newProduct.name" type="text" placeholder="Название товара"/>
      <input v-model="newProduct.price" type="number" step="0.01" placeholder="Цена"/>
      <textarea v-model="newProduct.description" placeholder="Описание"></textarea>
      <input type="file" @change="handleImageUpload" />

      <button @click="submitNewProduct" class="add-btn">Сохранить</button>
      <button @click="closeAddProductModal" class="close-btn">Отмена</button>
    </div>
  </div>
</template>

<script setup>
import {computed, reactive, ref, watch} from 'vue'
import axios from 'axios'
import debounce from 'lodash.debounce'

const cart = reactive(JSON.parse(localStorage.getItem('cart') || '{}'))
const page = ref(Number(localStorage.getItem('page')) || 1)
const searchQuery = ref(localStorage.getItem('searchQuery') || '')

const products = ref([])
const total = ref(0)
const limit = 6
const selectedProduct = ref(null)
const showModal = ref(false)

const errorMessage = ref('')
const showError = ref(false)

const totalPages = computed(() =>
    Math.ceil(total.value / limit)
)

const showAddModal = ref(false)

const newProduct = reactive({
  name: '',
  price: '',
  description: '',
  image: null
})

function openAddProductModal() {
  showAddModal.value = true
}

function closeAddProductModal() {
  showAddModal.value = false
  Object.assign(newProduct, {
    name: '',
    price: '',
    description: '',
    image: null
  })
}

async function submitNewProduct() {
  try {
    const formData = new FormData()
    formData.append('name', newProduct.name)
    formData.append('price', newProduct.price)
    formData.append('description', newProduct.description)
    if (newProduct.image) {
      formData.append('image', newProduct.image)
    }

    await axios.post('/api/products', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    closeAddProductModal()
    fetchProducts() // обновим список
  } catch (error) {
    console.error('Ошибка при добавлении товара:', error)
    showErrorPopup('Не удалось добавить товар. Проверьте данные.')
  }
}


async function fetchProducts() {
  try {
    const response = await axios.get('/api/products', {
      params: {
        search: searchQuery.value == null || searchQuery.value === "" ? null : "%" + searchQuery.value + "%",
        page: page.value - 1,
        size: limit
      }
    })
    console.log(response)
    products.value = response.data
    total.value = response.headers["x-total-count"]
  } catch (error) {
    showErrorPopup(error.response.data.errorMessage)
    console.error('Ошибка при загрузке товаров:', error.response.data.errorMessage)
  }
}

function showErrorPopup(message) {
  errorMessage.value = message

  showError.value = true

  setTimeout(() => {
    showError.value = false
  }, 5000)
}

function openProductModal(product) {
  selectedProduct.value = product
  showModal.value = true
}

function closeModal() {
  showModal.value = false
  selectedProduct.value = null
}

function handleImageUpload(event) {
  const file = event.target.files[0]
  const maxSize = 10 * 1024 * 1024

  if (file && file.size > maxSize) {
    showErrorPopup('Размер изображения не должен превышать 10 МБ.')
    newProduct.image = null
    event.target.value = ''
    return
  }

  newProduct.image = file
}


const onSearchInput = debounce(() => {
  page.value = 1
  fetchProducts()
}, 500)


watch(cart, (newCart) => {
  localStorage.setItem('cart', JSON.stringify(newCart))
}, {deep: true})

watch(page, (newPage) => {
  localStorage.setItem('page', newPage.toString())
  fetchProducts()
})

watch(searchQuery, (newQuery) => {
  localStorage.setItem('searchQuery', newQuery)
})


function addToCart(id) {
  cart[id] = 1
}

function removeFromCart(id) {
  delete cart[id]
}

function increaseQuantity(id) {
  cart[id]++
}

function decreaseQuantity(id) {
  cart[id] > 1 ? cart[id]-- : removeFromCart(id)
}


function nextPage() {
  if (page.value < totalPages.value) page.value++
}

function prevPage() {
  if (page.value > 1) page.value--
}


fetchProducts()
</script>

<style scoped>
.search-input {
  width: 100%;
  padding: 8px;
  margin-bottom: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.product-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.product-card {
  width: 200px;
  border: 1px solid #ddd;
  padding: 16px;
  border-radius: 8px;
  text-align: center;
}

.product-image-container {
  width: 100%;
  height: 150px;
  margin-bottom: 12px;
  border-radius: 4px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f0f0f0;
}

.product-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.placeholder-image {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  background-color: #f0f0f0;
}

.price {
  font-weight: bold;
  margin: 8px 0;
}

.add-btn,
.remove-btn {
  background-color: #2e7d32;
  color: white;
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.remove-btn {
  background-color: #c62828;
  margin-top: 8px;
}

.quantity-control {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-bottom: 8px;
}

.pagination {
  margin-top: 24px;
  display: flex;
  justify-content: center;
  gap: 12px;
}

.pagination button {
  padding: 6px 12px;
  border-radius: 4px;
  background: #1976d2;
  color: white;
  border: none;
  cursor: pointer;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.modal {
  background: white;
  padding: 24px;
  border-radius: 8px;
  max-width: 400px;
  width: 100%;
  text-align: center;
}

.close-btn {
  margin-top: 16px;
  background-color: #c62828;
  color: white;
  border: none;
  padding: 8px 16px;
  cursor: pointer;
  border-radius: 4px;
}

.error-popup {
  position: fixed;
  top: 20px;
  right: 20px;
  background-color: #f44336;
  color: white;
  padding: 12px 20px;
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
  z-index: 1000;
  animation: fadein 0.3s ease, fadeout 0.3s ease 2.7s;
}

.add-product-btn {
  background-color: #1976d2;
  color: white;
  padding: 10px 16px;
  border: none;
  border-radius: 6px;
  margin-bottom: 20px;
  cursor: pointer;
}

.modal input,
.modal textarea {
  width: 100%;
  margin: 8px 0;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

@keyframes fadein {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeout {
  from {
    opacity: 1;
    transform: translateY(0);
  }
  to {
    opacity: 0;
    transform: translateY(-10px);
  }
}

</style>
