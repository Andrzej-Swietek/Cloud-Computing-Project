import { useQuery } from "@tanstack/react-query"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { ScrollArea } from "@/components/ui/scroll-area"
import {User, UserControllerApiFactory} from "@/api"
import {Loading} from "@components/common";

async function fetchFeed(): Promise<User[]> {
    const {data} = await UserControllerApiFactory().getAllUsers()
    return data
}

export function FriendFeed() {
    const { data: feed = [], isLoading, isError, error } = useQuery({
        queryKey: ["friendFeed"],
        queryFn: fetchFeed,
        staleTime: 1000 * 60,
    })

    if (isLoading) return <Loading />
    if (isError) return <div>Error: {(error as Error).message}</div>

    return (
        <ScrollArea className="min-h-[600px] p-2">
            <div className="space-y-4">
                {feed.map((user) => (
                    <Card key={user.email} className="border-[1px] shadow-md hover:shadow-lg transition">
                        <CardHeader className="flex justify-between items-center">
                            <CardTitle>{user.firstname} {user.lastname}</CardTitle>
                            <div className="flex gap-2">
                                <Button size="sm" variant="outline" onClick={() => console.log("Like", user.email)}>
                                    👍
                                </Button>
                                <Button size="sm" variant="destructive" onClick={() => console.log("Dislike", user.email)}>
                                    👎
                                </Button>
                            </div>
                        </CardHeader>
                        <CardContent className="text-sm text-muted-foreground">
                            <p>Email: {user.email}</p>
                            {/* TODO  tutaj wyświetlić np. liczbę hobby, traitów, itp. jeśli są w obiekcie User */}
                            <p>Hobbies: {user.hobbies?.length ?? 0}</p>
                            <p>Traits: {user.traits?.length ?? 0}</p>
                        </CardContent>
                    </Card>
                ))}
            </div>
        </ScrollArea>
    )
}