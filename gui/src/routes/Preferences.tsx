import {useState} from "react";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";
import {Button} from "@/components/ui/button";
import {
    useGetAllFoods,
    useGetAllHobbies,
    useGetAllMovies,
    useGetAllTraits,
    useGetCurrentUser
} from "@/hooks/api-hooks.ts";
import {FoodRelationship, UserControllerApiFactory} from "@/api";
import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";


interface Item {
    id: string;
    name: string;
    liked: boolean | null;
    score?: number | null;
    type: "food" | "hobby" | "movie" | "trait";
}

export function Preferences() {
    const [items, setItems] = useState<Item[]>([]);
    const [loading, setLoading] = useState<boolean>(false);

    const {data: foods = []} = useGetAllFoods()
    const {data: hobbies = []} = useGetAllHobbies()
    const {data: movies = []} = useGetAllMovies()
    const {data: traits = []} = useGetAllTraits()
    const {data: currentUser} = useGetCurrentUser()

    // @ts-ignore
    const {data: fullUserData} = useQuery({
            queryFn: async () => {
                const {data} = await UserControllerApiFactory().getUser(currentUser?.id ?? '');
                // @ts-ignore
                const combined: Item[] = [
                    ...foods.map(f => ({
                        id: f.name,
                        name: f.name,
                        type: "food",
                        liked: data.foods.some((rel: FoodRelationship) => rel.food.name === f.name)
                    })),
                    ...hobbies.map(h => ({
                        id: h.name,
                        name: h.name,
                        type: "hobby",
                        liked: data.hobbies.includes(h)
                    })),
                    ...movies.map(m => ({
                        id: m.title,
                        name: m.title,
                        type: "movie",
                        liked: data.likedMovies.some(element => element.movie.title === m.title)
                    })),
                    ...traits.map(t => ({
                        id: t.name,
                        name: t.name,
                        type: "trait",
                        liked: data.traits.includes(t)
                    })),
                ];
                setItems(combined);
                setLoading(false);
                return data;
            },
            queryKey: ["currentUserDetails"],
            enabled: !!currentUser,
        }
    )


    const handleAction = (id: string, action: "like" | "dislike" | "remove") => {
        setItems(prev =>
            prev.map(item => {
                if (item.id === id) {
                    if (action === "like") return {...item, liked: true};
                    if (action === "dislike") return {...item, liked: false};
                    if (action === "remove") return null;
                }
                return item;
            }).filter(Boolean) as Item[]
        );

        const item = items.find(i => i.id === id);
        if (!item) return;

        switch (item.type) {
            case "food":
                console.log("Wywołanie API dla food", action, id);
                break;
            case "hobby":
                console.log("Wywołanie API dla hobby", action, id);
                break;
            case "movie":
                console.log("Wywołanie API dla movie", action, id);
                break;
            case "trait":
                console.log("Wywołanie API dla trait", action, id);
                break;
        }
    };


    return (
        <div className="p-4 lg:px-16 my-16 min-h-[90vh]">
            <h2 className="text-2xl mb-4">Lista rzeczy do polubienia/odrzucenia</h2>
            {loading ? (
                <p>Ładowanie...</p>
            ) : (
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead>Nazwa</TableHead>
                            <TableHead>Status</TableHead>
                            <TableHead>Akcje</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {items.map(item => (
                            <TableRow key={item.id}>
                                <TableCell>{item.name}</TableCell>
                                <TableCell>
                                    {item.liked === null ? "Nie ocenione" : item.liked ? "👍 Lubię" : "👎 Nie lubię"}
                                </TableCell>
                                <TableCell className="flex gap-2">
                                    <Button size="sm" variant="default"
                                            onClick={() => handleAction(item.id, "like")}>Like</Button>
                                    <Button size="sm" variant="destructive"
                                            onClick={() => handleAction(item.id, "dislike")}>Dislike</Button>
                                    <Button size="sm" variant="outline"
                                            onClick={() => handleAction(item.id, "remove")}>Remove</Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            )}
        </div>
    );
}

export const useActionMutation = (type: "food" | "hobby" | "movie" | "trait", action: "like" | "dislike" | "remove") => {
    const queryClient = useQueryClient();

    const mutationFn = async (id: string) => {
        switch (type) {
            case "food":
                if (action === "remove") return;
                if (action === "like")
                    return UserControllerApiFactory().likeFood(id, 5);
                else
                    return UserControllerApiFactory().dislikeFood(id);
            case "hobby":
                if (action === "remove") return UserControllerApiFactory().removeHobby(id);
                return UserControllerApiFactory().addHobby(id);
            case "movie":
                if (action === "remove") return UserControllerApiFactory().dislikeMovie(id);
                return UserControllerApiFactory().likeMovie(id, 5);
            case "trait":
                if (action === "remove") return UserControllerApiFactory().removeTrait(id);
                return UserControllerApiFactory().addTrait(id);
        }
    };

    return useMutation({
        mutationFn,
        onSuccess: () => {
            queryClient.invalidateQueries([`${type}s`]);
        },
    });
};