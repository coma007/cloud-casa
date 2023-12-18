
import { useLocation } from "react-router-dom";
import axios from "axios";
import { Credentials, NewPassword, UserRegister } from "../types/User";
import { CHANGE_PASSWORD, LOGIN, REGISTER, REGISTER_ADMIN, SUPER_AND_INIT } from "../../../../api";
import { jwtDecode } from "jwt-decode";

export const AuthService = {

  login: async (credentials: Credentials): Promise<string> => {
    let url = LOGIN();
    let response = await axios.post(url, credentials, {
      headers: {
      "Content-Type": 'application/json',
      }
    });
    return response.data;
  },

  register: async (user: UserRegister): Promise<string> => {
    let url = REGISTER();
    let response = await axios.post(url, user, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    return response.data;
  },

  registerAdmin: async (credentials: Credentials): Promise<string> => {
    let url = REGISTER_ADMIN();
    let response = await axios.post(url, credentials);
    return response.data;
  },

  // COULD RETURN EMPTY STRING
  changePassword: async (newPassword: NewPassword): Promise<string> => {
    let url = CHANGE_PASSWORD();
    let response = await axios.put(url, newPassword);
    let token = response.data;
    if(token !== ''){
      localStorage.setItem("token", token);
      window.location.reload();
    }
    return token;
  },

  logout: () => {
    console.log("Usao")
    localStorage.removeItem("token")
  },

  getRole: () =>{
    let token = localStorage.getItem("token");
    if(token === null) return null;
    const user = jwtDecode(token); 
    // console.log(user["role"][0]["name"]);
    return user["role"][0]["name"]
  },

  getUsername: () =>{
    let token = localStorage.getItem("token");
    if(token === null) return null;
    const user = jwtDecode(token); 
    // console.log(user["role"][0]["name"]);
    return user.sub;
  },


  // getUserData: async (): Promise<User> => {
  //   let url = GET_USER_URL();
  //   let response = await axios.get(url);
  //   return response.data;
  // },
}

axios.interceptors.request.use(
  config => {
    const token = localStorage.getItem("token");
    if(token !== undefined && token !== null && token !== ""){
      const user = jwtDecode(token!); 
      let currentDate = new Date();
      if (user.exp  === undefined || user.exp * 1000 < currentDate.getTime()){
        localStorage.removeItem("token");
        window.location.reload();
      }
    }

    if (config.url && !config.url.includes("maps.googleapis.com")) {
      if (token) {
        config.headers['Authorization'] ='Bearer ' + token;
      }
    }
    return config
  },
  error => {
    Promise.reject(error)
  }
)