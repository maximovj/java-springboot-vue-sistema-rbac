import { definePreset } from '@primeuix/themes'; // Asegúrate de tener esta dependencia
import Aura from '@primeuix/themes/aura';

export default definePreset(Aura, {
  semantic: {
    primary: {
      50: '#FFF7D6',
      100: '#FFEDAA',
      200: '#FFE27D',
      300: '#FFD850',
      400: '#FFCE2A',
      500: '#F4C430', // PRINCIPAL
      600: '#D4A017',
      700: '#B38610',
      800: '#8F6B0A',
      900: '#6B5005'
    },
    secondary: {
      success: '#2E7D32',
      info: '#0288D1',
      warning: '#F4C430',
      danger: '#B71C1C'
    },
    colorScheme: {
      light: {
        surface: {
          0: '#FFFFFF',
          50: '#F9FAF7',
          100: '#F3F4F6',
          200: '#E5E7EB'
        },
        text: {
          color: '#111111',
          'muted-color': '#6B7280'
        },
        border: {
          color: '#2A2A2A'
        },
        focus: {
          'ring-color': 'rgba(244, 196, 48, 0.45)'
        }
      },
      dark: {
        surface: {
          0: '#1a1a1a',
          50: '#2a2a2a',
          100: '#3a3a3a',
          200: '#4a4a4a'
        },
        text: {
          color: '#FFFFFF',
          'muted-color': '#A0A0A0'
        },
        border: {
          color: '#c01717ff'
        },
      }
    }
  }
})