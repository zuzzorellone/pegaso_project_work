import { useState } from "react";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import Box from "@mui/material/Box";
import IconButton from "@mui/material/IconButton";
import Drawer from "@mui/material/Drawer";
import List from "@mui/material/List";
import ListItemButton from "@mui/material/ListItemButton";
import LocalHospitalIcon from '@mui/icons-material/LocalHospital';
import MenuIcon from '@mui/icons-material/Menu';
import {AppBar} from "@mui/material";
import '../styles/Navbar.css';

const NAV_LINKS = [
    { href: "/bookAppointment", label: "Prenota una visita" },
    { href: "/downloadReport", label: "Scarica Referto" },
    { href: "/doctor/login", label: "Area Personale Medico" }
];

function Navbar() {
    const [menuOpen, setMenuOpen] = useState(false);

    return (
        <AppBar position="static" className="navbar">
            <Toolbar className="navbar__toolbar">
                <LocalHospitalIcon className="navbar__icon" />

                <Typography variant="h6" component="div" className="navbar__brand">
                    Salute e Benessere Lazio
                </Typography>

                <Box component="span" className="navbar__links">
                    {NAV_LINKS.map(link => (
                        <Button key={link.href} href={link.href} className="navbar__link">{link.label}</Button>
                    ))}
                </Box>

                <IconButton
                    className="navbar__menu-btn"
                    aria-label="Apri il menu"
                    onClick={() => setMenuOpen(true)}
                >
                    <MenuIcon />
                </IconButton>
            </Toolbar>

            <Drawer anchor="right" open={menuOpen} onClose={() => setMenuOpen(false)}>
                <List className="navbar__drawer-list">
                    {NAV_LINKS.map(link => (
                        <ListItemButton
                            key={link.href}
                            href={link.href}
                            onClick={() => setMenuOpen(false)}
                            className="navbar__drawer-item"
                        >
                            {link.label}
                        </ListItemButton>
                    ))}
                </List>
            </Drawer>
        </AppBar>
    );
}

export default Navbar;