import { BrowserRouter, Route, Routes } from "react-router-dom";
import { NonAuthGuard, AuthGuard } from "./GuardedRoute";
import EstateOverview from "../features/estate/overview/EstateOverview";
import RegisterPage from "../features/estate/register/RegisterPage";

const Router = () => {

    return (
        <BrowserRouter>
            <Routes>
                <Route element={<NonAuthGuard />}>
                    <Route index element={<EstateOverview />} />
                    <Route path="/register-real-estate" element={<RegisterPage />} />
                </Route>
                <Route element={<AuthGuard />}>
                    {/* <Route index element={<EstateOverview />} /> */}
                </Route>
            </Routes>
        </BrowserRouter>
    );
};

export default Router;