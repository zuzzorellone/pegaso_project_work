import Navbar from "../components/Navbar.jsx";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import DownloadIcon from '@mui/icons-material/Download';
import VerifiedUserIcon from '@mui/icons-material/VerifiedUser';
import '../styles/HomePage.css';

const FEATURES = [
    {
        icon: CalendarMonthIcon,
        title: "Prenota online",
        text: "Scegli il tipo di visita, la data e l'orario più adatti a te. La conferma arriva subito via email."
    },
    {
        icon: DownloadIcon,
        title: "Scarica il referto",
        text: "Accedi ai tuoi referti medici in modo sicuro con il tuo codice fiscale e il token ricevuto."
    },
    {
        icon: VerifiedUserIcon,
        title: "Dati protetti",
        text: "I tuoi dati personali e clinici sono trattati con i più elevati standard di sicurezza."
    }
];

function HomePage() {
    return (
        <>
            <Navbar />

            <Box className="hero">
                <Container className="hero__content" maxWidth="md">
                    <Typography variant="h1" component="h1" className="hero__title">
                        Prenota una visita,<br />scarica il tuo referto
                    </Typography>
                    <Typography variant="body1" className="hero__subtitle">
                        Un portale semplice per gestire le tue visite mediche, dalla prenotazione al ritiro
                        dei risultati, senza code né telefonate.
                    </Typography>
                    <Box className="hero__actions">
                        <Button variant="contained" href="/bookAppointment" className="hero__btn-primary">
                            Prenota una visita
                        </Button>
                        <Button variant="text" href="/downloadReport" className="hero__btn-secondary">
                            Scarica il tuo referto
                        </Button>
                    </Box>
                </Container>
            </Box>

            <Container className="home-features" maxWidth="md">
                {FEATURES.map((feature) => {
                    const FeatureIcon = feature.icon;
                    return (
                        <Box key={feature.title} className="home-features__item">
                            <FeatureIcon className="home-features__icon" />
                            <Box>
                                <Typography variant="h6" className="home-features__title">{feature.title}</Typography>
                                <Typography variant="body2" className="home-features__text">{feature.text}</Typography>
                            </Box>
                        </Box>
                    );
                })}
            </Container>
        </>
    );
}

export default HomePage;
