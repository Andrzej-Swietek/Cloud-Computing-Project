import type React from "react"

import {useState} from "react"
import {Button} from "@/components/ui/button"
import {Input} from "@/components/ui/input"
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card"
import {Alert, AlertDescription} from "@/components/ui/alert"
import {useLogin} from "@/hooks/api-hooks.ts";
import {useNavigate} from "react-router";

export function Login() {
    const navigate = useNavigate()
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const loginMutation = useLogin()

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        loginMutation.mutate(
            {email, password},
            {
                onSuccess: () => {
                    navigate("/feed")
                },
            },
        )

    }

    return (
        <div
            className="min-h-screen flex items-center justify-center bg-gradient-to-br from-background via-background to-secondary/20 px-4">
            <Card className="w-full max-w-md border-0 shadow-xl">
                <CardHeader className="space-y-1 pb-6">
                    <div className="flex items-center justify-center mb-4">
                        <div
                            className="w-10 h-10 bg-gradient-to-br from-primary to-accent rounded-lg flex items-center justify-center">
                            <span className="text-white font-bold text-lg">❤</span>
                        </div>
                    </div>
                    <CardTitle className="text-2xl text-center">Welcome Back</CardTitle>
                    <CardDescription className="text-center">Sign in to your account</CardDescription>
                </CardHeader>
                <CardContent>
                    <form onSubmit={handleSubmit} className="space-y-4">
                        {loginMutation.isError && (
                            <Alert variant="destructive">
                                <AlertDescription>
                                    {loginMutation.error.message}
                                </AlertDescription>
                            </Alert>
                        )}

                        <div className="space-y-2">
                            <label htmlFor="email" className="text-sm font-medium text-foreground">
                                Email
                            </label>
                            <Input
                                id="email"
                                type="email"
                                placeholder="Enter your email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                                className="h-10"
                            />
                        </div>

                        <div className="space-y-2">
                            <label htmlFor="password" className="text-sm font-medium text-foreground">
                                Password
                            </label>
                            <Input
                                id="password"
                                type="password"
                                placeholder="Enter your password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                                className="h-10"
                            />
                        </div>

                        <Button
                            type="submit"
                            className="w-full h-10 bg-gradient-to-r from-primary to-accent hover:shadow-lg transition-all"
                            disabled={loginMutation.isPending}
                        >
                            {loginMutation.isPending ? "Signing in..." : "Sign In"}
                        </Button>
                    </form>

                    <div className="mt-6 text-center text-sm text-muted-foreground">
                        Don't have an account?{" "}
                        <a href="/register" className="text-primary font-semibold hover:underline">
                            Create one
                        </a>
                    </div>
                </CardContent>
            </Card>
        </div>
    )
}
