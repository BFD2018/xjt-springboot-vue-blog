const sessionCache = {
  set (key, value) {
    if (!window.sessionStorage) {
      return
    }
    if (key != null && value != null) {
      window.sessionStorage.setItem(key, value)
    }
  },
  get (key) {
    if (!window.sessionStorage) {
      return null
    }
    if (key == null) {
      return null
    }
    return window.sessionStorage.getItem(key)
  },
  setJSON (key, jsonValue) {
    if (jsonValue != null) {
      this.set(key, JSON.stringify(jsonValue))
    }
  },
  getJSON (key) {
    const value = this.get(key)
    if (value != null) {
      return JSON.parse(value)
    }
  },
  remove (key) {
    window.sessionStorage.removeItem(key);
  }
}
const localCache = {
  set (key, value) {
    if (!window.localStorage) {
      return
    }
    if (key != null && value != null) {
      window.localStorage.setItem(key, value)
    }
  },
  get (key) {
    if (!window.localStorage) {
      return null
    }
    if (key == null) {
      return null
    }
    return window.localStorage.getItem(key)
  },
  setJSON (key, jsonValue) {
    if (jsonValue != null) {
      this.set(key, JSON.stringify(jsonValue))
    }
  },
  getJSON (key) {
    const value = this.get(key)
    if (value != null) {
      return JSON.parse(value)
    }
  },
  remove (key) {
    window.localStorage.removeItem(key);
  }
}

export default {
  /**
   * 会话级缓存
   */
  session: sessionCache,
  /**
   * 本地缓存
   */
  local: localCache
}
