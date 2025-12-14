import {HouseWifi} from 'lucide-react';
import {FC} from 'react';
import {NavLink} from 'react-router-dom';
// import {useTranslation} from 'react-i18next';


interface NavbarProps {
}

export const Navbar: FC<NavbarProps> = () => {
    // const {t} = useTranslation();

    return (
        <header
            className="sticky top-0 z-50 w-full border-b border-border/40 bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
            <div className="flex h-18 w-full items-center justify-between px-6">
                <div className="flex items-center gap-8">
                    <NavLink to="/" className="flex items-center gap-2">
                        <div
                            className="h-10 w-10 rounded-xl bg-gradient-to-br from-primary to-accent flex items-center justify-center">
                            <HouseWifi className="h-6 w-6 text-primary-foreground"/>
                        </div>
                        <span className="text-xl font-semibold tracking-tight text-foreground">Friendcommender</span>
                    </NavLink>

                    <div className="hidden md:flex items-center gap-6">
                        <NavLink
                            to="/feed"
                            className="text-md font-medium text-muted-foreground transition-colors hover:text-foreground"
                        >
                            Feed
                        </NavLink>
                    </div>
                </div>
            </div>
        </header>
    );
};
