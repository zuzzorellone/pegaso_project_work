import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { CssBaseline, ThemeProvider, StyledEngineProvider, createTheme } from '@mui/material';
import '@fontsource/roboto/400.css';
import '@fontsource/roboto/500.css';
import '@fontsource/roboto/700.css';
import './styles/global.css'
import App from './App.jsx'

const BLUE = '#5AA8E6';
const BLUE_TEXT = '#2A74AD';

const theme = createTheme({
  palette: {
    primary: { main: BLUE, dark: '#4A9DDC', light: '#EAF4FC', contrastText: '#1F2D3D' },
    background: { default: '#F5FAFE', paper: '#FFFFFF' },
    text: { primary: '#1F2D3D', secondary: '#5B6B7C' },
    divider: '#D6E4F0'
  },
  shape: { borderRadius: 8 },
  typography: {
    fontFamily: "'Roboto', sans-serif",
    button: { textTransform: 'none', fontWeight: 600 }
  },
  components: {
    // l'azzurro principale è troppo chiaro per il testo: dove il colore è testo si usa la variante scura
    MuiButton: { styleOverrides: { textPrimary: { color: BLUE_TEXT }, outlinedPrimary: { color: BLUE_TEXT } } },
    MuiTab: { styleOverrides: { root: { '&.Mui-selected': { color: BLUE_TEXT } } } },
    MuiInputLabel: { styleOverrides: { root: { '&.Mui-focused': { color: BLUE_TEXT } } } }
  }
});

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <StyledEngineProvider injectFirst>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <App />
      </ThemeProvider>
    </StyledEngineProvider>
  </StrictMode>,
)
