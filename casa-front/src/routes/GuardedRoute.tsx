import { Outlet, Navigate } from "react-router-dom";
import { jwtDecode } from 'jwt-decode' // import dependency

export const AuthGuard = () => {
    const auth = localStorage.getItem("token");
    return auth !== null ? <Outlet /> : <Navigate to="/login" />
}

export const NonAuthGuard = () => {
    const auth = localStorage.getItem("token");
    return auth === null ? <Outlet /> : <Navigate to="/" />
}

export const AdminAuthGuard = () => {
    return getRole() === "admin" ? <Outlet /> : <Navigate to="/login" />
}

export const RegularUserAuthGuard = () => {
    return getRole() === "regular user" ? <Outlet /> : <Navigate to="/login" />
}

const getRole = () => {
    const auth = localStorage.getItem("token");
    try {
        const decodedToken: any = jwtDecode(auth!);
        const roleName = decodedToken?.role[0].name;
        if (roleName) {
            return roleName
        } else {
            console.error("Role name not found in the token.");
        }
    } catch (error) {
        console.error("Error decoding the token");
    }
}