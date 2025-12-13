import {useMutation, useQuery} from "@tanstack/react-query"
import {
    AuthControllerApiFactory,
    FoodControllerApiFactory,
    HobbyControllerApiFactory,
    MovieControllerApiFactory,
    PersonalityTraitControllerApiFactory,
    RegisterRequest,
    UserControllerApiFactory
} from "@/api"

export const useLogin = () => {
    return useMutation({
        mutationFn: async (credentials: { email: string; password: string }) => {
            const {data} = await AuthControllerApiFactory().login(credentials)
            if (data.token) {
                localStorage.setItem("authToken", data.token)
            }
            return data
        },
    })
}


export const useRegister = () => {
    return useMutation({
        mutationFn: async (userData: RegisterRequest) => {
            const {data} = await AuthControllerApiFactory().register(userData)
            return data
        },
    })
}

export const useGetAllUsers = () => {
    return useQuery({
        queryKey: ["users"],
        queryFn: async () => {
            const {data} = await UserControllerApiFactory().getAllUsers();
            return data;
        },
    })
}


export const useGetAllFoods = () => {
    return useQuery({
        queryKey: ["foods"],
        queryFn: async () => {
            const {data} = await FoodControllerApiFactory().getAll3()
            return data
        },
    })
}

export const useGetAllHobbies = () => {
    return useQuery({
        queryKey: ["hobbies"],
        queryFn: async () => {
            const {data} = await HobbyControllerApiFactory().getAll2()
            return data
        },
    })
}

export const useGetAllMovies = () => {
    return useQuery({
        queryKey: ["movies"],
        queryFn: async () => {
            const {data} = await MovieControllerApiFactory().getAll1()
            return data
        },
    })
}

export const useGetAllTraits = () => {
    return useQuery({
        queryKey: ["traits"],
        queryFn: async () => {
            const {data} = await PersonalityTraitControllerApiFactory().getAll()
            return data
        },
    })
}
