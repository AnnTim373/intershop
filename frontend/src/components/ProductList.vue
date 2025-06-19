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

    <div class="top-actions">
      <button class="add-product-btn" @click="openAddProductModal">
        ➕ Добавить товар
      </button>
      <div class="order-cart-wrapper">
        <button class="orders-btn" @click="openOrdersModal">
          📦 Заказы
        </button>
        <button class="cart-btn" @click="openCartModal">
          🛒 Корзина ({{ cartItemCount }})
        </button>
      </div>

    </div>

    <div class="controls">
      <label>
        Показывать по:
        <select v-model="pageSize" @change="onPageSizeChange">
          <option :value="10">10</option>
          <option :value="20">20</option>
          <option :value="50">50</option>
          <option :value="100">100</option>
        </select>
      </label>

      <label>
        Сортировать по:
        <select v-model="sortOption" @change="fetchProducts">
          <option value="">Без сортировки</option>
          <option value="name,asc">Название (А-Я)</option>
          <option value="name,desc">Название (Я-А)</option>
          <option value="price,asc">Цена ↑</option>
          <option value="price,desc">Цена ↓</option>
        </select>
      </label>
    </div>

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
              <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
              <circle cx="8.5" cy="8.5" r="1.5"/>
              <path d="M21 15l-5-5L5 21"/>
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
      <input type="file" @change="handleImageUpload"/>

      <button @click="submitNewProduct" class="add-btn">Сохранить</button>
      <button @click="closeAddProductModal" class="close-btn">Отмена</button>
    </div>
  </div>
  <div v-if="showCartModal" class="modal-overlay" @click.self="closeCartModal">
    <div class="modal">
      <h2>Корзина</h2>

      <div v-if="cartItems.length === 0">
        <p>Корзина пуста</p>
      </div>

      <div v-else>
        <ul class="cart-items">
          <li v-for="item in cartItems" :key="item.id">
            {{ item.name }} — {{ item.price }} ₽ × {{ item.quantity }} = {{ item.price * item.quantity }} ₽
            <div class="quantity-control">
              <button @click.stop="decreaseQuantity(item.id)">-</button>
              <button @click.stop="increaseQuantity(item.id)">+</button>
            </div>
            <button class="remove-btn" @click.stop="removeFromCart(item.id)">
              Удалить из корзины
            </button>
          </li>
        </ul>
        <p>Баланс: {{ balance }}</p>
        <p><strong>Итого: {{ totalPrice }} ₽</strong></p>

        <button :disabled="!canPlaceOrder" class="checkout-btn { disabled: !canPlaceOrder }" @click="checkout">Оформить заказ</button>
      </div>

      <button class="close-btn" @click="closeCartModal">Закрыть</button>
    </div>
  </div>
  <div v-if="showOrdersModal" class="modal-overlay" @click.self="closeOrdersModal">
    <div class="modal orders-modal">
      <h2>Список заказов</h2>

      <div v-if="orders.length === 0">Нет заказов</div>
      <div v-else>
        <div v-for="order in orders" :key="order.id" class="order-entry">
          <div @click="openOrderDetailsModal(order)" class="order-summary">
            🧾 Заказ #{{ order.id }} — {{ order.totalPrice }} ₽
          </div>
        </div>
      </div>

      <button @click="closeOrdersModal" class="close-btn">Закрыть</button>
    </div>
  </div>
  <div v-if="showOrderDetailsModal" class="modal-overlay" @click.self="closeOrderDetailsModal">
    <div class="modal order-detail-modal">
      <h2>Заказ #{{ selectedOrder.id }}</h2>
      <p><strong>Сумма:</strong> {{ selectedOrder.totalPrice }} ₽</p>

      <ul>
        <li v-for="item in selectedOrder.contents" :key="item.id">
          {{ item.productName }} — {{ item.price }} ₽ × {{ item.quantity }} = {{ item.price * item.quantity }} ₽
        </li>
      </ul>

      <button class="close-btn" @click="closeOrderDetailsModal">Закрыть</button>
    </div>
  </div>
</template>

<script setup>
import {computed, reactive, ref, watch, onMounted} from 'vue'
import axios from 'axios'
import debounce from 'lodash.debounce'

const cart = reactive(JSON.parse(localStorage.getItem('cart') || '{}'))
const page = ref(Number(localStorage.getItem('page')) || 1)
const searchQuery = ref(localStorage.getItem('searchQuery') || '')
const pageSize = ref(localStorage.getItem('pageSize') || 10)
const sortOption = ref(localStorage.getItem('sortOption') || '')

const products = ref([])
const total = ref(0)
const selectedProduct = ref(null)
const showModal = ref(false)

const errorMessage = ref('')
const showError = ref(false)

const totalPages = computed(() =>
    Math.ceil(total.value / pageSize.value)
)

const showAddModal = ref(false)

const newProduct = reactive({
  name: '',
  price: '',
  description: '',
  image: null
})

const showCartModal = ref(false)

function openCartModal() {
  showCartModal.value = true
}

function closeCartModal() {
  showCartModal.value = false
}

const cartItems = computed(() => {
  return Object.entries(cart).map(([id, quantity]) => {
    const product = products.value.find(p => p.id == id)
    return product ? {...product, quantity} : null
  }).filter(Boolean)
})

const totalPrice = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
})

const cartItemCount = computed(() => {
  return Object.values(cart).reduce((sum, count) => sum + count, 0)
})

async function checkout() {
  try {
    const orderItems = cartItems.value.map(item => ({
      productId: item.id,
      quantity: item.quantity
    }))

    const response = await axios.post('/api/orders', {
      accountId: 1,
      items: orderItems
    })

    showCartModal.value = false
    selectedOrder.value = response.data
    showOrderDetailsModal.value = true
    for (const id in cart) delete cart[id]
    showErrorPopup('Заказ успешно оформлен!')
  } catch (error) {
    showErrorPopup('Ошибка при оформлении заказа.')
    console.error('Ошибка оформления заказа:', error)
  }
  await setBalance();
}

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
    fetchProducts()
  } catch (error) {
    console.error('Ошибка при добавлении товара:', error)
    showErrorPopup('Не удалось добавить товар. Проверьте данные.')
  }
}

const balance = ref(0);

async function fetchProducts() {
  try {
    const response = await axios.get('/api/products', {
      params: {
        search: searchQuery.value ? `%${searchQuery.value}%` : null,
        page: page.value - 1,
        size: pageSize.value,
        sort: sortOption.value
      }
    })
    products.value = response.data
    total.value = response.headers["x-total-count"]
  } catch (error) {
    showErrorPopup(error.response?.data?.errorMessage || "Ошибка загрузки товаров")
    console.error('Ошибка при загрузке товаров:', error)
  }
}

onMounted(async () => {
  await setBalance();
});

const canPlaceOrder = computed(() => balance.value >= totalPrice.value);

function showErrorPopup(message) {
  errorMessage.value = message

  showError.value = true

  setTimeout(() => {
    showError.value = false
  }, 5000)
}

async function setBalance() {
  const response = await axios.get('/api/account/1/balance');
  balance.value = response.data.balance;
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

const showOrdersModal = ref(false)
const orders = ref([])
const expandedOrderId = ref(null)

function openOrdersModal() {
  fetchOrders()
  showOrdersModal.value = true
}

function closeOrdersModal() {
  showOrdersModal.value = false
  expandedOrderId.value = null
}

async function fetchOrders() {
  try {
    const response = await axios.get('/api/orders')
    orders.value = response.data.map(order => ({
      ...order,
      totalPrice: order.contents.reduce((sum, item) => sum + item.price * item.quantity, 0)
    }))
  } catch (error) {
    console.error('Ошибка при получении заказов:', error)
    showErrorPopup('Не удалось загрузить заказы.')
  }
}

const onSearchInput = debounce(() => {
  page.value = 1
  fetchProducts()
}, 500)

const showOrderDetailsModal = ref(false)
const selectedOrder = ref(null)

function openOrderDetailsModal(order) {
  selectedOrder.value = order
  showOrderDetailsModal.value = true
}

function closeOrderDetailsModal() {
  selectedOrder.value = null
  showOrderDetailsModal.value = false
}

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

watch(pageSize, (newPageSize) => {
  localStorage.setItem('pageSize', newPageSize)
})

watch(sortOption, (newSortOption) => {
  localStorage.setItem('sortOption', newSortOption)
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

function onPageSizeChange() {
  page.value = 1
  fetchProducts()
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

.modal input,
.modal textarea {
  width: 100%;
  margin: 8px 0;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

.cart-items {
  text-align: left;
  margin-bottom: 12px;
}

.checkout-btn {
  background-color: #388e3c;
  color: white;
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 12px;
}

.top-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.orders-btn,
.add-product-btn,
.cart-btn {
  margin-left: 10px;
  background-color: #1976d2;
  color: white;
  padding: 10px 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 16px;
}

.checkout-btn:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
  color: #666666;
  pointer-events: none;
}

.order-cart-wrapper {
  margin-left: auto;
}

.orders-modal {
  max-height: 80vh;
  overflow-y: auto;
  text-align: left;
}

.order-entry {
  border: 1px solid #ccc;
  padding: 12px;
  margin-bottom: 10px;
  border-radius: 6px;
  background: #f9f9f9;
}

.order-summary {
  font-weight: bold;
  cursor: pointer;
}

.order-detail-modal {
  max-height: 80vh;
  overflow-y: auto;
  text-align: left;
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
