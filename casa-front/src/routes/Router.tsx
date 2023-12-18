import { BrowserRouter, Route, Routes } from "react-router-dom";
import { NonAuthGuard, AuthGuard, AdminAuthGuard, RegularUserAuthGuard, SuperAdminAuthGuard, SuperAdminInitAuthGuard } from "./GuardedRoute";
import EstateRegisterPage from "../features/estate/register/RegisterPage";
import LoginPage, { Logout } from "../features/user/auth/pages/LoginPage/LoginPage";
import RegisterPage from "../features/user/auth/pages/RegisterPage/RegisterPage";
import StepperForm from "../features/device/register/DeviceRegistrationStepper";
import RegisterAdminPage from "../features/user/auth/pages/RegisterAdminPage/RegisterAdminPage";
import ProfilePage from "../features/user/auth/pages/ProfilePage/ProfilePage";
import EstateOverviewPage from "../features/estate/overview/EstateOverviewPage";
import RequestOverviewPage from "../features/request/overview/RequestOverviewPage";
import DeviceOverviewPage from "../features/device/overview/DeviceOverviewPage";
import DeviceDetails from "../features/device/details/DeviceDetails";

const Router = () => {

    return (
        <BrowserRouter>
            <Routes>
                <Route path="/register-device" element={<StepperForm />} />
                <Route path="/device-details" element={<DeviceDetails />} />

                <Route element={<NonAuthGuard />}>
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/register" element={<RegisterPage />} />
                </Route>
                <Route element={<AdminAuthGuard />}>
                    <Route path="/" element={<RequestOverviewPage />} />
                    <Route path="/requests" element={<RequestOverviewPage />} />
                    <Route path="/logout/admin" element={<Logout />} />
                </Route>
                <Route element={<SuperAdminAuthGuard />}>
                    <Route path="/register/admin" element={<RegisterAdminPage />} />
                    <Route path="/" element={<RegisterAdminPage />} />
                    <Route path="/logout/super-admin" element={<Logout />} />
                </Route>
                <Route element={<SuperAdminInitAuthGuard />}>
                    <Route path="/init/register/admin" element={<RegisterAdminPage />} />
                    <Route path="/" element={<RegisterAdminPage />} />
                    <Route path="/logout/super-admin-init" element={<Logout />} />
                </Route>
                <Route element={<RegularUserAuthGuard />}>
                    <Route path="/" element={<EstateOverviewPage />} />
                    <Route path="/real-estate-overview" element={<EstateOverviewPage />} />
                    <Route path="/device-overview" element={<DeviceOverviewPage />} />
                    <Route path="/reigster-device" element={<StepperForm />} />
                    <Route path="/register-real-estate" element={<EstateRegisterPage />} />
                    <Route path="/profile" element={<ProfilePage />} />
                    <Route path="/logout" element={<Logout />} />
                </Route>
            </Routes>
        </BrowserRouter >

    );
};

export default Router;