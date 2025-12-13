import {Button} from "@components/ui/button.tsx";
import {useNavigate} from "react-router";

export const GoBackButton = () => {
    const navigate = useNavigate();
    return (
        <Button className="cursor-pointer" variant="secondary" onClick={() => navigate(-1)}>
            &larr; Back
        </Button>
    );
}