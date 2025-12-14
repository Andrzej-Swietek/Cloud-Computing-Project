import {useState} from "react"
import {useGetAllFoods, useGetAllHobbies, useGetAllMovies, useGetAllTraits, useRegister} from "@/hooks/api-hooks"
import {Button} from "@/components/ui/button"
import {Input} from "@/components/ui/input"
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card"
import {Alert, AlertDescription} from "@/components/ui/alert"
import {Checkbox} from "@/components/ui/checkbox"
import type {FoodRelationshipRequest, Hobby, MovieRelationshipRequest, PersonalityTrait, RegisterRequest} from "@/api"
import {useNavigate} from "react-router";

export function Register() {
    const navigate = useNavigate()
    const registerMutation = useRegister()
    const {data: foods = []} = useGetAllFoods()
    const {data: hobbies = []} = useGetAllHobbies()
    const {data: movies = []} = useGetAllMovies()
    const {data: traits = []} = useGetAllTraits()

    // Form state
    const [step, setStep] = useState<number>(0)
    const [formData, setFormData] = useState<RegisterRequest>({
        firstname: "",
        lastname: "",
        email: "",
        password: "",
        traits: [] as string[],
        hobbies: [] as string[],
        likedMovies: [] as MovieRelationshipRequest[],
        dislikedMovies: [] as MovieRelationshipRequest[],
        foods: [] as FoodRelationshipRequest[],
        dislikeFood: [] as FoodRelationshipRequest[],
        likedUsers: [] as string[],
        dislikedUsers: [] as string[],
    })

    const [foodScores, setFoodScores] = useState<{ [key: string]: number }>({})
    const [dislikeFoodScores, setDislikeFoodScores] = useState<{ [key: string]: number }>({})
    const [movieScores, setMovieScores] = useState<{ [key: string]: number }>({})
    const [dislikeMovieScores, setDislikeMovieScores] = useState<{ [key: string]: number }>({})

    const handleBasicInfoChange = (field: string, value: string) => {
        setFormData({...formData, [field]: value})
    }

    const toggleTrait = (trait: PersonalityTrait) => {
        setFormData((prev: RegisterRequest) => ({
            ...prev,
            traits: prev.traits.find((t) => t === trait.name)
                ? prev.traits.filter((t) => t !== trait.name)
                : [...prev.traits, trait.name!],
        }))
    }

    const toggleHobby = (hobby: Hobby) => {
        setFormData((prev: RegisterRequest) => ({
            ...prev,
            hobbies: prev.hobbies.find((h) => h === hobby.name)
                ? prev.hobbies.filter((h) => h !== hobby.name)
                : [...prev.hobbies, hobby.name!],
        }))
    }

    const toggleLikedFood = (foodId: string) => {
        const food = foods.find((f) => f.name === foodId)
        if (!food) return

        if (formData.foods.find((f) => f.foodId === foodId)) {
            setFormData((prev) => ({
                ...prev,
                foods: prev.foods.filter((f) => f.foodId !== foodId),
            }))
            setFoodScores((prev) => {
                const newScores = {...prev}
                delete newScores[foodId]
                return newScores
            })
        } else {
            setFormData((prev) => ({
                ...prev,
                foods: [...prev.foods, {foodId, score: 5}],
            }))
            setFoodScores((prev) => ({...prev, [foodId]: 5}))
        }
    }

    const toggleDislikedFood = (foodId: string) => {
        const food = foods.find((f) => f.name === foodId)
        if (!food) return

        if (formData.dislikeFood.find((f) => f.foodId === foodId)) {
            setFormData((prev) => ({
                ...prev,
                dislikeFood: prev.dislikeFood.filter((f) => f.foodId !== foodId),
            }))
            setDislikeFoodScores((prev) => {
                const newScores = {...prev}
                delete newScores[foodId]
                return newScores
            })
        } else {
            setFormData((prev: RegisterRequest) => ({
                ...prev,
                dislikeFood: [...prev.dislikeFood, {foodId, score: 5}],
            }))
            setDislikeFoodScores((prev) => ({...prev, [foodId]: 5}))
        }
    }

    const toggleLikedMovie = (movieId: string) => {
        const movie = movies.find((m) => m.title === movieId)
        if (!movie) return

        if (formData.likedMovies.find((m) => m.movieId === movieId)) {
            setFormData((prev) => ({
                ...prev,
                likedMovies: prev.likedMovies.filter((m) => m.movieId !== movieId),
            }))
            setMovieScores((prev) => {
                const newScores = {...prev}
                delete newScores[movieId]
                return newScores
            })
        } else {
            setFormData((prev) => ({
                ...prev,
                likedMovies: [...prev.likedMovies, {movieId, score: 5}],
            }))
            setMovieScores((prev) => ({...prev, [movieId]: 5}))
        }
    }

    const toggleDislikedMovie = (movieId: string) => {
        const movie = movies.find((m) => m.title === movieId)
        if (!movie) return

        if (formData.dislikedMovies.find((m) => m.movieId === movieId)) {
            setFormData((prev) => ({
                ...prev,
                dislikedMovies: prev.dislikedMovies.filter((m) => m.movieId !== movieId),
            }))
            setDislikeMovieScores((prev) => {
                const newScores = {...prev}
                delete newScores[movieId]
                return newScores
            })
        } else {
            setFormData((prev) => ({
                ...prev,
                dislikedMovies: [...prev.dislikedMovies, {movieId, score: 5}],
            }))
            setDislikeMovieScores((prev) => ({...prev, [movieId]: 5}))
        }
    }

    const updateFoodScore = (foodId: string, score: number, isDisliked: boolean) => {
        if (isDisliked) {
            setDislikeFoodScores((prev) => ({...prev, [foodId]: score}))
            setFormData((prev) => ({
                ...prev,
                dislikeFood: prev.dislikeFood.map((f) => (f.foodId === foodId ? {...f, score} : f)),
            }))
        } else {
            setFoodScores((prev) => ({...prev, [foodId]: score}))
            setFormData((prev) => ({
                ...prev,
                foods: prev.foods.map((f) => (f.foodId === foodId ? {...f, score} : f)),
            }))
        }
    }

    const updateMovieScore = (movieId: string, score: number, isDisliked: boolean) => {
        if (isDisliked) {
            setDislikeMovieScores((prev) => ({...prev, [movieId]: score}))
            setFormData((prev) => ({
                ...prev,
                dislikedMovies: prev.dislikedMovies.map((m) => (m.movieId === movieId ? {...m, score} : m)),
            }))
        } else {
            setMovieScores((prev) => ({...prev, [movieId]: score}))
            setFormData((prev) => ({
                ...prev,
                likedMovies: prev.likedMovies.map((m) => (m.movieId === movieId ? {...m, score} : m)),
            }))
        }
    }

    const handleSubmit = async () => {
        registerMutation.mutate(formData, {
            onSuccess: () => {
                navigate("/feed")
            },
        })
    }

    const steps = [
        {label: "Basic Info", id: "basic"},
        {label: "Personality", id: "personality"},
        {label: "Hobbies", id: "hobbies"},
        {label: "Food Preferences", id: "food"},
        {label: "Movie Preferences", id: "movies"},
    ]

    return (
        <div
            className="min-h-screen flex items-center justify-center bg-gradient-to-br from-background via-background to-secondary/20 px-4 py-8">
            <Card className="w-full max-w-2xl border-0 shadow-xl">
                <CardHeader className="space-y-1 pb-6 text-center">
                    <div className="flex items-center justify-center mb-4">
                        <div
                            className="w-10 h-10 bg-gradient-to-br from-primary to-accent rounded-lg flex items-center justify-center">
                            <span className="text-white font-bold text-lg">❤</span>
                        </div>
                    </div>
                    <CardTitle className="text-2xl">Create Your Profile</CardTitle>
                    <CardDescription>
                        Step {step + 1} of {steps.length}
                    </CardDescription>
                </CardHeader>

                <CardContent>
                    {registerMutation.isError && (
                        <Alert variant="destructive" className="mb-6">
                            <AlertDescription>
                                {registerMutation.error instanceof Error ? registerMutation.error.message : "Registration failed"}
                            </AlertDescription>
                        </Alert>
                    )}

                    {/* Step 0: Basic Info */}
                    {step === 0 && (
                        <div className="space-y-4">
                            <div className="grid grid-cols-2 gap-4">
                                <div className="space-y-2">
                                    <label className="text-sm font-medium">First Name</label>
                                    <Input
                                        placeholder="John"
                                        value={formData.firstname}
                                        onChange={(e) => handleBasicInfoChange("firstname", e.target.value)}
                                    />
                                </div>
                                <div className="space-y-2">
                                    <label className="text-sm font-medium">Last Name</label>
                                    <Input
                                        placeholder="Doe"
                                        value={formData.lastname}
                                        onChange={(e) => handleBasicInfoChange("lastname", e.target.value)}
                                    />
                                </div>
                            </div>
                            <div className="space-y-2">
                                <label className="text-sm font-medium">Email</label>
                                <Input
                                    type="email"
                                    placeholder="john@example.com"
                                    value={formData.email}
                                    onChange={(e) => handleBasicInfoChange("email", e.target.value)}
                                />
                            </div>
                            <div className="space-y-2">
                                <label className="text-sm font-medium">Password</label>
                                <Input
                                    type="password"
                                    placeholder="••••••••"
                                    value={formData.password}
                                    onChange={(e) => handleBasicInfoChange("password", e.target.value)}
                                />
                            </div>
                        </div>
                    )}

                    {/* Step 1: Personality Traits */}
                    {step === 1 && (
                        <div className="space-y-4">
                            <div className="grid grid-cols-2 gap-3 max-h-[400px] overflow-y-auto">
                                {traits.map((trait) => (
                                    <div key={trait.name} className="flex items-center space-x-2">
                                        <Checkbox
                                            id={`trait-${trait.name}`}
                                            checked={formData.traits.some((t) => t === trait.name)}
                                            onCheckedChange={() => toggleTrait(trait)}
                                        />
                                        <label htmlFor={`trait-${trait.name}`} className="text-sm cursor-pointer flex-1">
                                            {trait.name}
                                        </label>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}

                    {/* Step 2: Hobbies */}
                    {step === 2 && (
                        <div className="space-y-4">
                            <div className="grid grid-cols-2 gap-3 max-h-[400px] overflow-y-auto">
                                {hobbies.map((hobby) => (
                                    <div key={hobby.name} className="flex items-start space-x-2">
                                        <Checkbox
                                            id={`hobby-${hobby.name}`}
                                            checked={formData.hobbies.some((h) => h === hobby.name)}
                                            onCheckedChange={() => toggleHobby(hobby)}
                                        />
                                        <div className="flex-1 cursor-pointer" onClick={() => toggleHobby(hobby)}>
                                            <label htmlFor={`hobby-${hobby.name}`}
                                                   className="text-sm font-medium block cursor-pointer">
                                                {hobby.name}
                                            </label>
                                            <p className="text-xs text-muted-foreground">{hobby.description}</p>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}

                    {/* Step 3: Food Preferences */}
                    {step === 3 && (
                        <div className="space-y-6">
                            <div className="space-y-4">
                                <div>
                                    <h3 className="text-lg font-semibold mb-3">Foods You Like</h3>
                                    <div className="grid grid-cols-2 gap-3 max-h-[300px] overflow-y-auto pb-4">
                                        {foods.map((food) => (
                                            <div key={`like-${food.name}`} className="space-y-2">
                                                <div className="flex items-center space-x-2">
                                                    <Checkbox
                                                        id={`food-like-${food.name}`}
                                                        checked={formData.foods.some((f) => f.foodId === food.name)}
                                                        onCheckedChange={() => toggleLikedFood(food.name)}
                                                    />
                                                    <label htmlFor={`food-like-${food.name}`}
                                                           className="text-sm cursor-pointer flex-1">
                                                        {food.name}
                                                    </label>
                                                </div>
                                                {formData.foods.some((f) => f.foodId === food.name) && (
                                                    <div className="ml-6 space-y-1">
                                                        <div className="text-xs text-muted-foreground">
                                                            Preference: {foodScores[food.name] || 5}/10
                                                        </div>
                                                        <input
                                                            type="range"
                                                            min="1"
                                                            max="10"
                                                            value={foodScores[food.name] || 5}
                                                            onChange={(e) => updateFoodScore(food.name, Number.parseInt(e.target.value), false)}
                                                            className="w-full h-1"
                                                        />
                                                    </div>
                                                )}
                                            </div>
                                        ))}
                                    </div>
                                </div>

                                <div className="border-t pt-4">
                                    <h3 className="text-lg font-semibold mb-3">Foods You Dislike</h3>
                                    <div className="grid grid-cols-2 gap-3 max-h-[300px] overflow-y-auto">
                                        {foods.map((food) => (
                                            <div key={`dislike-${food.name}`} className="space-y-2">
                                                <div className="flex items-center space-x-2">
                                                    <Checkbox
                                                        id={`food-dislike-${food.name}`}
                                                        checked={formData.dislikeFood.some((f) => f.foodId === food.name)}
                                                        onCheckedChange={() => toggleDislikedFood(food.name)}
                                                    />
                                                    <label htmlFor={`food-dislike-${food.name}`}
                                                           className="text-sm cursor-pointer flex-1">
                                                        {food.name}
                                                    </label>
                                                </div>
                                                {formData.dislikeFood.some((f) => f.foodId === food.name) && (
                                                    <div className="ml-6 space-y-1">
                                                        <div className="text-xs text-muted-foreground">
                                                            Dislike level: {dislikeFoodScores[food.name] || 5}/10
                                                        </div>
                                                        <input
                                                            type="range"
                                                            min="1"
                                                            max="10"
                                                            value={dislikeFoodScores[food.name || 0] || 5}
                                                            onChange={(e) => updateFoodScore(food.name, Number.parseInt(e.target.value), true)}
                                                            className="w-full h-1"
                                                        />
                                                    </div>
                                                )}
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            </div>
                        </div>
                    )}

                    {/* Step 4: Movie Preferences */}
                    {step === 4 && (
                        <div className="space-y-6">
                            <div className="space-y-4">
                                <div>
                                    <h3 className="text-lg font-semibold mb-3">Movies You Like</h3>
                                    <div className="grid grid-cols-2 gap-3 max-h-[300px] overflow-y-auto pb-4">
                                        {movies.map((movie) => (
                                            <div key={`like-movie-${movie.title}`} className="space-y-2">
                                                <div className="flex items-start space-x-2">
                                                    <Checkbox
                                                        id={`movie-like-${movie.title}`}
                                                        checked={formData.likedMovies.some((m) => m.movieId === movie.title)}
                                                        onCheckedChange={() => toggleLikedMovie(movie.title)}
                                                    />
                                                    <div className="flex-1 cursor-pointer">
                                                        <label
                                                            htmlFor={`movie-like-${movie.title}`}
                                                            className="text-sm font-medium block cursor-pointer"
                                                        >
                                                            {movie.title}
                                                        </label>
                                                        <p className="text-xs text-muted-foreground">{movie.genre}</p>
                                                    </div>
                                                </div>
                                                {formData.likedMovies.some((m) => m.movieId === movie.title) && (
                                                    <div className="ml-6 space-y-1">
                                                        <div className="text-xs text-muted-foreground">
                                                            Rating: {movieScores[movie.title || 0] || 5}/10
                                                        </div>
                                                        <input
                                                            type="range"
                                                            min="1"
                                                            max="10"
                                                            value={movieScores[movie.title || 0] || 5}
                                                            onChange={(e) => updateMovieScore(movie.title, Number.parseInt(e.target.value), false)}
                                                            className="w-full h-1"
                                                        />
                                                    </div>
                                                )}
                                            </div>
                                        ))}
                                    </div>
                                </div>

                                <div className="border-t pt-4">
                                    <h3 className="text-lg font-semibold mb-3">Movies You Dislike</h3>
                                    <div className="grid grid-cols-2 gap-3 max-h-[300px] overflow-y-auto">
                                        {movies.map((movie) => (
                                            <div key={`dislike-movie-${movie.title}`} className="space-y-2">
                                                <div className="flex items-start space-x-2">
                                                    <Checkbox
                                                        id={`movie-dislike-${movie.title}`}
                                                        checked={formData.dislikedMovies.some((m) => m.movieId === movie.title)}
                                                        onCheckedChange={() => toggleDislikedMovie(movie.title)}
                                                    />
                                                    <div className="flex-1 cursor-pointer">
                                                        <label
                                                            htmlFor={`movie-dislike-${movie.title}`}
                                                            className="text-sm font-medium block cursor-pointer"
                                                        >
                                                            {movie.title}
                                                        </label>
                                                        <p className="text-xs text-muted-foreground">{movie.genre}</p>
                                                    </div>
                                                </div>
                                                {formData.dislikedMovies.some((m) => m.movieId === movie.title) && (
                                                    <div className="ml-6 space-y-1">
                                                        <div className="text-xs text-muted-foreground">
                                                            Dislike level: {dislikeMovieScores[movie.title] || 5}/10
                                                        </div>
                                                        <input
                                                            type="range"
                                                            min="1"
                                                            max="10"
                                                            value={dislikeMovieScores[movie.title] || 5}
                                                            onChange={(e) => updateMovieScore(movie.title, Number.parseInt(e.target.value), true)}
                                                            className="w-full h-1"
                                                        />
                                                    </div>
                                                )}
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            </div>
                        </div>
                    )}

                    {/* Navigation Buttons */}
                    <div className="flex gap-3 mt-8 pt-6 border-t">
                        <Button
                            variant="outline"
                            onClick={() => setStep(Math.max(0, step - 1))}
                            disabled={step === 0}
                            className="flex-1"
                        >
                            Back
                        </Button>
                        {step < steps.length - 1 ? (
                            <Button
                                onClick={() => setStep(step + 1)}
                                className="flex-1 bg-gradient-to-r from-primary to-accent hover:shadow-lg transition-all"
                            >
                                Next
                            </Button>
                        ) : (
                            <Button
                                onClick={handleSubmit}
                                disabled={registerMutation.isPending}
                                className="flex-1 bg-gradient-to-r from-primary to-accent hover:shadow-lg transition-all"
                            >
                                {registerMutation.isPending ? "Creating Account..." : "Create Account"}
                            </Button>
                        )}
                    </div>

                    <div className="mt-6 text-center text-sm text-muted-foreground">
                        Already have an account?{" "}
                        <a href="/login" className="text-primary font-semibold hover:underline">
                            Sign in
                        </a>
                    </div>
                </CardContent>
            </Card>
        </div>
    )
}
