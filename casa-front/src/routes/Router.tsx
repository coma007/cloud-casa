import { BrowserRouter, Route, Routes } from "react-router-dom";
import { NonAuthGuard, AuthGuard } from "./GuardedRoute";
import EstateOverview from "../features/estate/overview/EstateOverview";

const Router = () => {

    return (
        <BrowserRouter>
            <Routes>
                <Route element={<NonAuthGuard />}>
                    {/* <Route path="/login" element={</>} /> */}
                    <Route index element={<EstateOverview />} />
                </Route>
                <Route element={<AuthGuard />}>
                    {/* <Route index element={<EstateOverview />} /> */}
                </Route>
            </Routes>
        </BrowserRouter>
    );
};

export default Router;