import './App.css';
import { BrowserRouter } from 'react-router-dom';
import Routing from './components/routing/Routing';
import Navigation from './components/Navigation';
import { UserContextProvider } from './context/UserContext';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <UserContextProvider>
          <Navigation />
          <Routing />
        </UserContextProvider>
      </BrowserRouter>
    </div>
  );
}

export default App;
