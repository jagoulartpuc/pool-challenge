import { BrowserRouter, BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from './Login';
import Poll from './Poll';
import Results from './Results';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path='/' element={<Login />} />
        <Route path='/poll' element={<Poll />} />
        <Route path='/results' element={<Results />} />

      </Routes>
    </BrowserRouter>

  );
}

export default App;
