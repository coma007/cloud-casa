import axios, { AxiosResponse, AxiosError } from 'axios';

export type ServiceResponse<T> = {
    data: T;
    status: number;
};

export type ServiceError = {
    message: string | unknown;
    status?: number;
};

export const ApiService = {
    makeRequest: async function <T>(
        method: 'get' | 'post' | 'put' | 'patch' | 'delete',
        url: string,
        data?: any,
        params?: any
    ): Promise<ServiceResponse<T>> {
        try {
            const response: AxiosResponse<T> = await axios({
                method,
                url,
                data,
                params,
            });
            return {
                data: response.data,
                status: response.status,
            };
        } catch (error: any) {
            return Promise.reject(handleRequestError(error));
        }
    },
};

function handleRequestError(error: AxiosError): ServiceError {
    if (axios.isAxiosError(error)) {
        if (error.response) {
            return {
                message: error.response.data,
                status: error.response.status,
            };
        } else if (error.request) {
            return {
                message: 'No response received from the server.',
            };
        }
    }
    return { message: error.message };
}

