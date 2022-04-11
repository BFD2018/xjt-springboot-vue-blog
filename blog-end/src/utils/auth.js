import Config from '@/settings'

const TokenKey = Config.TokenKey;

export function getToken() {
  return JSON.parse(window.sessionStorage.getItem(TokenKey));
}

export function setToken(token, rememberMe) {
  // val必须是字符串
  // window.sessionStorage.setItem(key,val);
  // window.sessionStorage.setItem("LoginUser",JSON.stringify(payload));

  window.sessionStorage.setItem(TokenKey,JSON.stringify(token));
}

export function removeToken() {
  return window.sessionStorage.removeItem(TokenKey);
}
