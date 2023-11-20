
import { useLocation } from "react-router-dom";
import axios from "axios";
import { Credentials, UserRegister } from "../types/User";
import { LOGIN, REGISTER } from "../../../../api";

export const AuthService = {

  login: async (credentials: Credentials): Promise<string> => {
    let url = LOGIN();
    let response = await axios.post(url, credentials);
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

  logout: () => {
    localStorage.removeItem("token")
  },


  // getUserData: async (): Promise<User> => {
  //   let url = GET_USER_URL();
  //   let response = await axios.get(url);
  //   return response.data;
  // },
}

axios.interceptors.request.use(
  config => {
    const token = localStorage.getItem("token")
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