import { BrowserRouter, Route, Routes } from "react-router-dom";
import { NonAuthGuard, AuthGuard } from "./GuardedRoute";
import EstateOverview from "../features/estate/overview/EstateOverview";
import LoginPage from "../features/user/auth/pages/LoginPage/LoginPage";
import RegisterPage from "../features/user/auth/pages/RegisterPage/RegisterPage";

const Router = () => {

    return (
        <BrowserRouter>
            <Routes>
                <Route element={<NonAuthGuard />}>
                    <Route index element={<EstateOverview />} />
                </Route>
                <Route element={<AuthGuard />}>
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                    {/* <Route index element={<EstateOverview />} /> */}
                </Route>
            </Routes>
        </BrowserRouter>
    );
};

export default Router;