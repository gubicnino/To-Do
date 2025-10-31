import './App.css';
import { BrowserRouter } from 'react-router-dom';
import Routing from './components/routing/Routing';
import Navigation from './components/Navigation';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Navigation />
        <Routing />
      </BrowserRouter>
    </div>
  );
}

export default App;
