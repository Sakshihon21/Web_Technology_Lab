import React from "react";

function Events(){

const eventList = [

{
id:1,
name:"AI Workshop",
date:"20 March 2026",
location:"Seminar Hall",
fee:"200"
},

{
id:2,
name:"Hackathon",
date:"25 March 2026",
location:"Computer Lab",
fee:"500"
},

{
id:3,
name:"Robotics Club Activity",
date:"30 March 2026",
location:"Innovation Lab",
fee:"300"
}

];

return(

<div>

<h2>Upcoming College Events</h2>

<table>

<thead>

<tr>
<th>Event Name</th>
<th>Date</th>
<th>Location</th>
<th>Fee</th>
</tr>

</thead>

<tbody>

{eventList.map((event)=>(
<tr key={event.id}>

<td>{event.name}</td>
<td>{event.date}</td>
<td>{event.location}</td>
<td>{event.fee}</td>

</tr>
))}

</tbody>

</table>

</div>

);

}

export default Events;