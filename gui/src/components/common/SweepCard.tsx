import type React from "react"

import {useState} from "react"
import {motion, useMotionValue, useTransform, type PanInfo} from "framer-motion"
import {Card, CardContent} from "@/components/ui/card"
import {Badge} from "@/components/ui/badge"
import {Button} from "@/components/ui/button"
import {Heart, X, Sparkles, Film, Utensils} from "lucide-react"
import type {User} from "@/api"

interface SwipeCardProps {
    user: User
    onSwipe: (direction: "left" | "right") => void
    style?: React.CSSProperties
}

export function SwipeCard({user, onSwipe, style}: SwipeCardProps) {
    const [exitX, setExitX] = useState(0)
    const x = useMotionValue(0)
    const rotate = useTransform(x, [-200, 200], [-25, 25])
    const opacity = useTransform(x, [-200, -100, 0, 100, 200], [0, 1, 1, 1, 0])

    const handleDragEnd = (_: MouseEvent | TouchEvent | PointerEvent, info: PanInfo) => {
        if (Math.abs(info.offset.x) > 100) {
            setExitX(info.offset.x > 0 ? 200 : -200)
            onSwipe(info.offset.x > 0 ? "right" : "left")
        }
    }

    const handleLike = () => {
        setExitX(200)
        onSwipe("right")
    }

    const handleDislike = () => {
        setExitX(-200)
        onSwipe("left")
    }

    return (
        <motion.div
            style={{
                x,
                rotate,
                opacity,
                position: "absolute",
                ...style,
            }}
            drag="x"
            dragConstraints={{left: 0, right: 0}}
            onDragEnd={handleDragEnd}
            animate={exitX !== 0 ? {x: exitX * 2, opacity: 0} : {}}
            transition={{type: "spring", stiffness: 300, damping: 30}}
            className="w-full cursor-grab active:cursor-grabbing"
        >
            <Card
                className="w-full h-[600px] overflow-hidden shadow-2xl border-2 border-border/50 bg-gradient-to-br from-background via-background to-rose-50/30">
                <CardContent className="p-0 h-full flex flex-col">
                    {/* Header with gradient background */}
                    <div
                        className="relative h-48 bg-gradient-to-br from-green-500 via-cyan-500 to-blue-500 flex items-center justify-center">
                        <div className="absolute inset-0 bg-[url('/abstract-hearts-pattern.png')] opacity-10 bg-cover"/>
                        <div className="relative text-center z-10">
                            <h2 className="text-4xl font-bold text-white mb-2">
                                {user.firstname} {user.lastname}
                            </h2>
                            <p className="text-white/90 text-sm">{user.email}</p>
                        </div>
                    </div>

                    {/* Content area */}
                    <div className="flex-1 p-6 overflow-y-auto space-y-6">
                        {/* Personality Traits */}
                        {user.traits && user.traits.length > 0 && (
                            <div>
                                <div className="flex items-center gap-2 mb-3">
                                    <Sparkles className="w-5 h-5 text-rose-500"/>
                                    <h3 className="font-semibold text-lg">Personality</h3>
                                </div>
                                <div className="flex flex-wrap gap-2">
                                    {user.traits.map((trait) => (
                                        <Badge
                                            key={trait.name}
                                            variant={
                                                trait.type === "POSITIVE" ? "default" : trait.type === "NEGATIVE" ? "destructive" : "secondary"
                                            }
                                            className="text-xs"
                                        >
                                            {trait.name}
                                        </Badge>
                                    ))}
                                </div>
                            </div>
                        )}

                        {/* Hobbies */}
                        {user.hobbies && user.hobbies.length > 0 && (
                            <div>
                                <div className="flex items-center gap-2 mb-3">
                                    <Sparkles className="w-5 h-5 text-purple-500"/>
                                    <h3 className="font-semibold text-lg">Hobbies</h3>
                                </div>
                                <div className="space-y-2">
                                    {user.hobbies.slice(0, 3).map((hobby) => (
                                        <div key={hobby.name} className="bg-muted/50 rounded-lg p-3">
                                            <p className="font-medium text-sm">{hobby.name}</p>
                                            <p className="text-xs text-muted-foreground">{hobby.description}</p>
                                        </div>
                                    ))}
                                    {user.hobbies.length > 3 && (
                                        <p className="text-xs text-muted-foreground text-center">+{user.hobbies.length - 3} more
                                            hobbies</p>
                                    )}
                                </div>
                            </div>
                        )}

                        {/* Favorite Foods */}
                        {user.foods && user.foods.length > 0 && (
                            <div>
                                <div className="flex items-center gap-2 mb-3">
                                    <Utensils className="w-5 h-5 text-orange-500"/>
                                    <h3 className="font-semibold text-lg">Favorite Foods</h3>
                                </div>
                                <div className="flex flex-wrap gap-2">
                                    {user.foods.slice(0, 6).map((foodRel) => (
                                        <Badge key={foodRel.id} variant="outline" className="text-xs">
                                            {foodRel.food.name}
                                            <span className="ml-1 text-rose-500 font-bold">{foodRel.score}/10</span>
                                        </Badge>
                                    ))}
                                    {user.foods.length > 6 && (
                                        <Badge variant="secondary" className="text-xs">
                                            +{user.foods.length - 6} more
                                        </Badge>
                                    )}
                                </div>
                            </div>
                        )}

                        {/* Favorite Movies */}
                        {user.likedMovies && user.likedMovies.length > 0 && (
                            <div>
                                <div className="flex items-center gap-2 mb-3">
                                    <Film className="w-5 h-5 text-blue-500"/>
                                    <h3 className="font-semibold text-lg">Favorite Movies</h3>
                                </div>
                                <div className="space-y-2">
                                    {user.likedMovies.slice(0, 3).map((movieRel) => (
                                        <div key={movieRel.id}
                                             className="bg-muted/50 rounded-lg p-3 flex justify-between items-center">
                                            <div>
                                                <p className="font-medium text-sm">{movieRel.movie.title}</p>
                                                <p className="text-xs text-muted-foreground">
                                                    {movieRel.movie.genre} • {movieRel.movie.releaseYear}
                                                </p>
                                            </div>
                                            <Badge variant="default" className="bg-rose-500">
                                                {movieRel.score}/10
                                            </Badge>
                                        </div>
                                    ))}
                                    {user.likedMovies.length > 3 && (
                                        <p className="text-xs text-muted-foreground text-center">
                                            +{user.likedMovies.length - 3} more movies
                                        </p>
                                    )}
                                </div>
                            </div>
                        )}
                    </div>

                    {/* Action buttons */}
                    <div className="p-6 bg-gradient-to-t from-background to-transparent">
                        <div className="flex justify-center gap-6">
                            <Button
                                size="lg"
                                variant="outline"
                                className="h-16 w-16 rounded-full border-2 hover:border-red-500 hover:bg-red-50 transition-all bg-transparent"
                                onClick={handleDislike}
                            >
                                <X className="w-8 h-8 text-red-500"/>
                            </Button>
                            <Button
                                size="lg"
                                className="h-20 w-20 rounded-full bg-gradient-to-br from-rose-500 to-pink-600 hover:from-rose-600 hover:to-pink-700 shadow-lg hover:shadow-xl transition-all"
                                onClick={handleLike}
                            >
                                <Heart className="w-10 h-10 text-white fill-white"/>
                            </Button>
                        </div>
                    </div>
                </CardContent>
            </Card>
        </motion.div>
    )
}
