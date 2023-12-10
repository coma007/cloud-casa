import Router from './routes/Router';
import style from './App.module.scss'
import { Helmet } from "react-helmet";

function App() {
  return (
    <>
      <Helmet>
        <title>My App</title>
        <link rel="canonical" href="http://example.com" />
        <link rel="icon" href="data:image/x-icon;," type="image/x-icon" /> 
        <meta name="description" content="My app description" />
      </Helmet>
      <div className={style.App}>
        <Router></Router>
      </div>
    </>
  );
}

export default App;
