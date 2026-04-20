import React from "react";
import RegisterForm from "./RegisterForm";
import Events from "./Events";
import "./App.css";

function App(){

return(

<div>

<h1>College Event Management System</h1>

<div className="container">

<Events/>

<hr/>

<RegisterForm/>

</div>

</div>

);

}

export default App;