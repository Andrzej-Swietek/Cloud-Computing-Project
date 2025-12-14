import {useQuery} from "@tanstack/react-query"
import {Card, CardContent} from "@/components/ui/card"
import {Button} from "@/components/ui/button"
import {User, UserControllerApiFactory} from "@/api"
import {useState} from "react";
import {SwipeCard} from "@components/common/SweepCard.tsx";
import {Heart, RotateCcw, Sparkles, X} from "lucide-react";
import {useDislikeUser, useLikeUser} from "@/hooks/api-hooks.ts";

async function fetchFeed(): Promise<User[]> {
    const {data} = await UserControllerApiFactory().getAllUsers()
    return data
}

export function FriendFeed() {
    const { data: feed = [], isLoading, isError, error, refetch } = useQuery({
        queryKey: ["friendFeed"],
        queryFn: fetchFeed,
        staleTime: 1000 * 60,
    })

    const [currentIndex, setCurrentIndex] = useState(0)
    const [likedUsers, setLikedUsers] = useState<User[]>([])
    const [dislikedUsers, setDislikedUsers] = useState<User[]>([])


    const likeUserMutation = useLikeUser()
    const dislikeUserMutation = useDislikeUser()

    const handleSwipe = async (direction: "left" | "right") => {
        const currentUser = feed[currentIndex]

        if (direction === "right") {
            setLikedUsers([...likedUsers, currentUser])
            if (currentUser.email) {
                try {
                    await likeUserMutation.mutateAsync(currentUser.email)
                    console.log("[v0] Liked user:", currentUser.email)
                } catch (error) {
                    console.error("[v0] Error liking user:", error)
                }
            }
        } else {
            setDislikedUsers([...dislikedUsers, currentUser])
            if (currentUser.email) {
                try {
                    await dislikeUserMutation.mutateAsync(currentUser.email)
                    console.log("[v0] Disliked user:", currentUser.email)
                } catch (error) {
                    console.error("[v0] Error disliking user:", error)
                }
            }
        }

        setTimeout(() => {
            setCurrentIndex((prev) => prev + 1)
        }, 300)
    }

    const handleReset = () => {
        setCurrentIndex(0)
        setLikedUsers([])
        setDislikedUsers([])
        refetch()
    }

    if (isLoading) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-rose-50 via-pink-50 to-purple-50 flex items-center justify-center">
                <div className="text-center space-y-4">
                    <div className="animate-spin rounded-full h-16 w-16 border-4 border-rose-500 border-t-transparent mx-auto" />
                    <p className="text-muted-foreground">Loading potential matches...</p>
                </div>
            </div>
        )
    }

    if (isError) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-rose-50 via-pink-50 to-purple-50 flex items-center justify-center p-4">
                <Card className="max-w-md">
                    <CardContent className="p-6 text-center space-y-4">
                        <X className="w-12 h-12 text-red-500 mx-auto" />
                        <h2 className="text-xl font-semibold">Error Loading Feed</h2>
                        <p className="text-muted-foreground">{(error as Error).message}</p>
                        <Button onClick={() => refetch()} variant="outline">
                            Try Again
                        </Button>
                    </CardContent>
                </Card>
            </div>
        )
    }

    const hasMoreUsers = currentIndex < feed.length
    const currentUser = feed[currentIndex]

    return (
        <div className="min-h-screen bg-gradient-to-br from-green-100 via-blue-50 to-purple-50 py-8 px-4">
            <div className="max-w-2xl mx-auto">
                {/* Header */}
                <div className="mb-8 text-center">
                    <div className="flex items-center justify-center gap-2 mb-2">
                        <Sparkles className="w-6 h-6 text-green-500" />
                        <h1 className="text-3xl font-bold bg-gradient-to-r from-green-600 to-blue-600 bg-clip-text text-transparent">
                            Find Your Match
                        </h1>
                        <Sparkles className="w-6 h-6 text-green-500" />
                    </div>
                    <p className="text-muted-foreground">
                        {hasMoreUsers ? `${feed.length - currentIndex} potential matches waiting` : "You've seen all matches"}
                    </p>
                </div>

                {/* Stats Bar */}
                <div className="flex justify-center gap-4 mb-6">
                    <div className="flex items-center gap-2 bg-white/80 backdrop-blur-sm rounded-full px-4 py-2 shadow-sm">
                        <Heart className="w-4 h-4 text-rose-500 fill-rose-500" />
                        <span className="font-semibold text-sm">{likedUsers.length}</span>
                    </div>
                    <div className="flex items-center gap-2 bg-white/80 backdrop-blur-sm rounded-full px-4 py-2 shadow-sm">
                        <X className="w-4 h-4 text-red-500" />
                        <span className="font-semibold text-sm">{dislikedUsers.length}</span>
                    </div>
                </div>

                {/* Card Stack */}
                <div className="relative h-[600px] mb-6">
                    {hasMoreUsers ? (
                        <>
                            {/* Preview cards (stack effect) */}
                            {feed.slice(currentIndex + 1, currentIndex + 3).map((user, index) => (
                                <div
                                    key={user.email}
                                    className="absolute inset-0 w-full"
                                    style={{
                                        transform: `scale(${1 - (index + 1) * 0.05}) translateY(${(index + 1) * 10}px)`,
                                        opacity: 1 - (index + 1) * 0.3,
                                        zIndex: 10 - index,
                                    }}
                                >
                                    <Card className="w-full h-full bg-white/50 backdrop-blur-sm" />
                                </div>
                            ))}

                            {/* Active card */}
                            <SwipeCard
                                key={currentUser.email}
                                user={currentUser}
                                onSwipe={handleSwipe}
                                style={{ zIndex: 20 }}
                            />
                        </>
                    ) : (
                        <Card className="w-full h-full flex items-center justify-center bg-gradient-to-br from-white to-rose-50/30">
                            <CardContent className="text-center space-y-6 p-8">
                                <div className="w-24 h-24 mx-auto bg-gradient-to-br from-rose-500 to-pink-600 rounded-full flex items-center justify-center">
                                    <Sparkles className="w-12 h-12 text-white" />
                                </div>
                                <div className="space-y-2">
                                    <h2 className="text-2xl font-bold">All Caught Up!</h2>
                                    <p className="text-muted-foreground">You've reviewed all available matches.</p>
                                    <p className="text-sm text-muted-foreground">Check back later for new potential matches.</p>
                                </div>
                                <div className="pt-4 space-y-3">
                                    <Button onClick={handleReset} size="lg" className="w-full bg-gradient-to-r from-rose-500 to-pink-600">
                                        <RotateCcw className="w-4 h-4 mr-2" />
                                        Review Again
                                    </Button>
                                    <div className="grid grid-cols-2 gap-4 text-sm">
                                        <div className="bg-white/50 rounded-lg p-4">
                                            <p className="text-muted-foreground mb-1">You Liked</p>
                                            <p className="text-2xl font-bold text-rose-500">{likedUsers.length}</p>
                                        </div>
                                        <div className="bg-white/50 rounded-lg p-4">
                                            <p className="text-muted-foreground mb-1">You Passed</p>
                                            <p className="text-2xl font-bold text-gray-500">{dislikedUsers.length}</p>
                                        </div>
                                    </div>
                                </div>
                            </CardContent>
                        </Card>
                    )}
                </div>

                {/* Instructions */}
                {hasMoreUsers && (
                    <p className="text-center text-sm text-muted-foreground">
                        Swipe right to like • Swipe left to pass • Or use the buttons below
                    </p>
                )}
            </div>
        </div>
    )
}
