import { BrowserRouter, Route, Routes } from "react-router-dom";
import { NonAuthGuard, AdminAuthGuard, RegularUserAuthGuard } from "./GuardedRoute";
import EstateRegisterPage from "../features/estate/register/RegisterPage";
import LoginPage, { Logout } from "../features/user/auth/pages/LoginPage/LoginPage";
import RegisterPage from "../features/user/auth/pages/RegisterPage/RegisterPage";
import StepperForm from "../features/device/register/DeviceRegistrationStepper";
import ProfilePage from "../features/user/auth/pages/ProfilePage/ProfilePage";
import EstateOverviewPage from "../features/estate/overview/EstateOverviewPage";
import RequestOverviewPage from "../features/request/overview/RequestOverviewPage";
import DeviceOverviewPage from "../features/device/overview/DeviceOverviewPage";

const Router = () => {

    return (
        <BrowserRouter>
            <Routes>
                <Route path="/register-device" element={<StepperForm />} />

                <Route path="/requests" element={<RequestOverviewPage />} />
                <Route element={<NonAuthGuard />}>
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/register" element={<RegisterPage />} />
                </Route>
                <Route element={<AdminAuthGuard />}>
                    {/* <Route path="/requests" element={<RequestOverview />} /> */}
                </Route>
                <Route element={<RegularUserAuthGuard />}>
                    <Route path="/" element={<EstateOverviewPage />} />
                    <Route path="/real-estate-overview" element={<EstateOverviewPage />} />
                    <Route path="/device-overview" element={<DeviceOverviewPage />} />
                    <Route path="/register-real-estate" element={<EstateRegisterPage />} />
                    <Route path="/profile" element={<ProfilePage />} />
                    <Route path="/logout" element={<Logout />} />
                </Route>
            </Routes>
        </BrowserRouter >
    );
};

export default Router;