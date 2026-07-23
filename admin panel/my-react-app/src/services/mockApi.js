// Lightweight mock API for dashboard data (development only)
export function getDashboardStats() {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        stats: {
          totalUsers: 12450,
          activeUsers: 9870,
          blockedUsers: 45,
          premiumUsers: 3250,
          male: 6800,
          female: 5650,
          verified: 10234,
        },
        monthlyUsers: [800, 920, 1000, 1150, 1230, 1400, 1500, 1600, 1700, 1800, 1900, 2000],
        revenue: [4200, 5200, 6100, 7200, 8300, 9000, 10400, 11500, 12100, 13800, 14900, 15800],
        subscriptions: {
          basic: 6700,
          premium: 4200,
          elite: 1550,
        },
        recentActivities: [
          { id: 1, text: "New user signup: user_12401", time: "15 mins ago" },
          { id: 2, text: "Payment successful: TXN-20260715-001", time: "30 mins ago" },
          { id: 3, text: "Profile updated: user_12190", time: "1 hour ago" },
        ],
        recentPayments: [
          { id: "TXN-20260715-001", user: "user_1201", plan: "Premium", amount: 299, status: "Success", date: "2026-07-15" },
          { id: "TXN-20260715-002", user: "user_1202", plan: "Basic", amount: 79, status: "Pending", date: "2026-07-15" },
        ],
        supportTickets: [
          { id: 101, user: "user_1300", subject: "Account verification", priority: "High", status: "Open" },
          { id: 102, user: "user_1298", subject: "Payment issue", priority: "Medium", status: "Pending" },
        ],
        systemStatus: {
          api: "OK",
          db: "OK",
          redis: "Degraded",
        },
      });
    }, 600);
  });
}

// ------------------ Users mock API ------------------
let _users = Array.from({ length: 42 }).map((_, i) => ({
  id: `${1000 + i}`,
  name: `User ${i + 1}`,
  email: `user${i + 1}@example.com`,
  phone: `+1 555 01${String(i + 1).padStart(2, "0")}`,
  status: i % 10 === 0 ? "blocked" : "active",
  verified: i % 3 === 0,
  createdAt: new Date(Date.now() - i * 86400000).toISOString(),
}));

export function getUsers({ page = 1, pageSize = 10, search = "", status = "all" } = {}) {
  return new Promise((resolve) => {
    setTimeout(() => {
      let list = _users.slice();
      if (search) {
        const q = search.toLowerCase();
        list = list.filter((u) => u.name.toLowerCase().includes(q) || u.email.toLowerCase().includes(q));
      }
      if (status && status !== "all") {
        list = list.filter((u) => u.status === status);
      }
      const total = list.length;
      const start = (page - 1) * pageSize;
      const items = list.slice(start, start + pageSize);
      resolve({ items, total });
    }, 300);
  });
}

export function createUser(payload) {
  return new Promise((resolve) => {
    setTimeout(() => {
      const id = String(1000 + _users.length + 1);
      const record = { id, ...payload, createdAt: new Date().toISOString() };
      _users.unshift(record);
      resolve(record);
    }, 300);
  });
}

export function updateUser(id, payload) {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      const idx = _users.findIndex((u) => u.id === id);
      if (idx === -1) return reject(new Error("User not found"));
      _users[idx] = { ..._users[idx], ...payload };
      resolve(_users[idx]);
    }, 300);
  });
}

export function deleteUser(id) {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      const idx = _users.findIndex((u) => u.id === id);
      if (idx === -1) return reject(new Error("User not found"));
      const [removed] = _users.splice(idx, 1);
      resolve(removed);
    }, 300);
  });
}

export function getUserById(id) {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      const user = _users.find((u) => u.id === id);
      if (!user) return reject(new Error("User not found"));
      resolve(user);
    }, 200);
  });
}

export function getUserActivities(id) {
  // Simple mocked per-user activity list
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve([
        { id: 1, text: `Logged in from web`, time: "2 hours ago" },
        { id: 2, text: `Updated profile photo`, time: "1 day ago" },
        { id: 3, text: `Subscription started: Premium`, time: "2 weeks ago" },
      ]);
    }, 250);
  });
}

// ------------------ Payments mock API ------------------
let _payments = Array.from({ length: 12 }).map((_, i) => ({
  id: `${2000 + i}`,
  txnId: `TXN-202607${String(i + 1).padStart(2, "0")}-00${i}`,
  user: `user${i + 1}`,
  plan: i % 3 === 0 ? "Premium" : "Basic",
  amount: i % 3 === 0 ? 299 : 79,
  method: i % 2 === 0 ? "Stripe" : "PayPal",
  status: i % 5 === 0 ? "Failed" : i % 4 === 0 ? "Pending" : "Success",
  date: new Date(Date.now() - i * 86400000).toISOString().split("T")[0],
}));

export function getPayments({ page = 1, pageSize = 10, search = "", status = "all" } = {}) {
  return new Promise((resolve) => {
    setTimeout(() => {
      let list = _payments.slice();
      if (search) {
        const q = search.toLowerCase();
        list = list.filter((p) => p.txnId.toLowerCase().includes(q) || p.user.toLowerCase().includes(q));
      }
      if (status && status !== "all") list = list.filter((p) => p.status === status);
      const total = list.length;
      const start = (page - 1) * pageSize;
      const items = list.slice(start, start + pageSize);
      resolve({ items, total });
    }, 300);
  });
}

export function createPayment(payload) {
  return new Promise((resolve) => {
    setTimeout(() => {
      const id = String(2000 + _payments.length + 1);
      const record = { id, ...payload };
      _payments.unshift(record);
      resolve(record);
    }, 300);
  });
}

export function updatePayment(id, payload) {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      const idx = _payments.findIndex((p) => p.id === id);
      if (idx === -1) return reject(new Error("Payment not found"));
      _payments[idx] = { ..._payments[idx], ...payload };
      resolve(_payments[idx]);
    }, 300);
  });
}

export function deletePayment(id) {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      const idx = _payments.findIndex((p) => p.id === id);
      if (idx === -1) return reject(new Error("Payment not found"));
      const [removed] = _payments.splice(idx, 1);
      resolve(removed);
    }, 300);
  });
}

// ------------------ Orders mock API ------------------
let _orders = Array.from({ length: 20 }).map((_, i) => ({
  id: `${3000 + i}`,
  orderNumber: `ORD-202607${String(i + 1).padStart(2, "0")}-0${i}`,
  user: `user${(i % 12) + 1}`,
  total: Math.round((50 + i * 7) * 100) / 100,
  status: i % 6 === 0 ? "Cancelled" : i % 4 === 0 ? "Pending" : "Completed",
  date: new Date(Date.now() - i * 43200000).toISOString().split("T")[0],
}));

export function getOrders({ page = 1, pageSize = 10, search = "", status = "all" } = {}) {
  return new Promise((resolve) => {
    setTimeout(() => {
      let list = _orders.slice();
      if (search) {
        const q = search.toLowerCase();
        list = list.filter((o) => o.orderNumber.toLowerCase().includes(q) || o.user.toLowerCase().includes(q));
      }
      if (status && status !== "all") list = list.filter((o) => o.status === status);
      const total = list.length;
      const start = (page - 1) * pageSize;
      const items = list.slice(start, start + pageSize);
      resolve({ items, total });
    }, 300);
  });
}

export function createOrder(payload) {
  return new Promise((resolve) => {
    setTimeout(() => {
      const id = String(3000 + _orders.length + 1);
      const record = { id, ...payload };
      _orders.unshift(record);
      resolve(record);
    }, 300);
  });
}

export function updateOrder(id, payload) {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      const idx = _orders.findIndex((o) => o.id === id);
      if (idx === -1) return reject(new Error("Order not found"));
      _orders[idx] = { ..._orders[idx], ...payload };
      resolve(_orders[idx]);
    }, 300);
  });
}

export function deleteOrder(id) {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      const idx = _orders.findIndex((o) => o.id === id);
      if (idx === -1) return reject(new Error("Order not found"));
      const [removed] = _orders.splice(idx, 1);
      resolve(removed);
    }, 300);
  });
}

// ------------------ Products mock API ------------------
let _products = Array.from({ length: 18 }).map((_, i) => ({
  id: `${4000 + i}`,
  name: `Product ${i + 1}`,
  sku: `SKU-${1000 + i}`,
  price: Math.round((10 + i * 2.5) * 100) / 100,
  stock: 10 + i * 3,
  status: i % 5 === 0 ? "inactive" : "active",
  createdAt: new Date(Date.now() - i * 86400000).toISOString(),
}));

export function getProducts({ page = 1, pageSize = 10, search = "", status = "all" } = {}) {
  return new Promise((resolve) => {
    setTimeout(() => {
      let list = _products.slice();
      if (search) {
        const q = search.toLowerCase();
        list = list.filter((p) => p.name.toLowerCase().includes(q) || p.sku.toLowerCase().includes(q));
      }
      if (status && status !== "all") list = list.filter((p) => p.status === status);
      const total = list.length;
      const start = (page - 1) * pageSize;
      const items = list.slice(start, start + pageSize);
      resolve({ items, total });
    }, 300);
  });
}

export function createProduct(payload) {
  return new Promise((resolve) => {
    setTimeout(() => {
      const id = String(4000 + _products.length + 1);
      const record = { id, ...payload, createdAt: new Date().toISOString() };
      _products.unshift(record);
      resolve(record);
    }, 300);
  });
}

export function updateProduct(id, payload) {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      const idx = _products.findIndex((p) => p.id === id);
      if (idx === -1) return reject(new Error("Product not found"));
      _products[idx] = { ..._products[idx], ...payload };
      resolve(_products[idx]);
    }, 300);
  });
}

export function deleteProduct(id) {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      const idx = _products.findIndex((p) => p.id === id);
      if (idx === -1) return reject(new Error("Product not found"));
      const [removed] = _products.splice(idx, 1);
      resolve(removed);
    }, 300);
  });
}


