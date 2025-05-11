<template>
  <div>

    <input
        v-model="searchQuery"
        @input="onSearchInput"
        placeholder="Поиск товаров..."
        class="search-input"
    />

    <div class="product-list">
      <div v-for="product in products" :key="product.id" class="product-card">
        <img v-if="product.image"
             :src="`data:image/jpeg;base64,${product.image}`"
             alt="product.name"
             class="product-image"
        />
        <h3>{{ product.name }}</h3>
        <p class="price">{{ product.price }} ₽</p>

        <div v-if="cart[product.id]">
          <div class="quantity-control">
            <button @click="decreaseQuantity(product.id)">-</button>
            <span>{{ cart[product.id] }}</span>
            <button @click="increaseQuantity(product.id)">+</button>
          </div>
          <button class="remove-btn" @click="removeFromCart(product.id)">
            Удалить из корзины
          </button>
        </div>
        <div v-else>
          <button class="add-btn" @click="addToCart(product.id)">
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

const totalPages = computed(() =>
    Math.ceil(total.value / limit)
)


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
    console.error('Ошибка при загрузке товаров:', error)
  }
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

.product-image {
  width: 100%;
  height: auto;
  margin-bottom: 12px;
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
</style>
