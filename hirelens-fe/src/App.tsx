import "./App.css";
import AnalysisForm from "./components/AnalysisForm";

function App() {
  return (
    <div className="page-shell">
      <div className="bg-orb orb-1" />
      <div className="bg-orb orb-2" />

      <div className="content-wrap">
        <div className="hero-card">
          <AnalysisForm />
        </div>
      </div>
    </div>
  );
}

export default App;