import { BrowserRouter, Route, Routes } from "react-router-dom";
import { NonAuthGuard, AuthGuard, AdminAuthGuard, RegularUserAuthGuard } from "./GuardedRoute";
import EstateOverview from "../features/estate/overview/EstateOverview";
import EstateRegisterPage from "../features/estate/register/RegisterPage";
import LoginPage from "../features/user/auth/pages/LoginPage/LoginPage";
import RegisterPage from "../features/user/auth/pages/RegisterPage/RegisterPage";
import RequestOverview from "../features/request/overview/RequestOverview";
import StepperForm from "../features/device/register/DeviceRegistrationStepper";

const Router = () => {

    return (
        <BrowserRouter>
            <Routes>
                <Route path="/register-device" element={<StepperForm/>}/>
                <Route element={<NonAuthGuard />}>
<<<<<<< Updated upstream
=======
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/register" element={<RegisterPage />} />
                </Route>
                <Route element={<AdminAuthGuard />}>
                    <Route path="/requests" element={<RequestOverview />} />
                </Route>
                <Route element={<RegularUserAuthGuard />}>
>>>>>>> Stashed changes
                    <Route index element={<EstateOverview />} />
                    <Route path="/register-real-estate" element={<EstateRegisterPage />} />
                </Route>
            </Routes>
        </BrowserRouter >
    );
};

export default Router;