import {Link} from 'react-router';
import {Button} from "@/components/ui/button"

export function Home() {
    return (
        <main className="min-h-screen bg-gradient-to-br from-background via-background to-secondary/20">
            <nav className="flex justify-between items-center p-6 max-w-7xl mx-auto">
                <div className="flex items-center gap-2">
                    <div className="w-8 h-8 bg-gradient-to-br from-primary to-accent rounded-lg flex items-center justify-center">
                        <span className="text-white font-bold">❤</span>
                    </div>
                    <span className="font-bold text-xl text-primary">Matchmake</span>
                </div>
                <div className="flex gap-3">
                    <Link to="/login">
                        <Button variant="ghost">Sign In</Button>
                    </Link>
                    <Link to="/register">
                        <Button className="bg-gradient-to-r from-primary to-accent hover:shadow-lg">Get Started</Button>
                    </Link>
                </div>
            </nav>

            <section className="max-w-7xl mx-auto px-6 py-20 text-center">
                <h1 className="text-5xl font-bold mb-6 text-balance">
                    Find Your{" "}
                    <span className="bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">
            Perfect Connection
          </span>
                </h1>
                <p className="text-xl text-muted-foreground mb-8 max-w-2xl mx-auto text-balance">
                    Connect with people who share your passions, values, and interests. Build meaningful relationships through
                    personalized matching.
                </p>
                <Link to="/register">
                    <Button size="lg" className="bg-gradient-to-r from-primary to-accent hover:shadow-lg text-lg px-8 h-12">
                        Start Finding Matches
                    </Button>
                </Link>
            </section>
        </main>
    )
}
