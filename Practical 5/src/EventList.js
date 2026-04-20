import React from "react";

function EventList(){

const events=[
{id:1,name:"AI Workshop",date:"20 March"},
{id:2,name:"Hackathon",date:"25 March"},
{id:3,name:"Robotics Club Activity",date:"30 March"}
];

return(
<div>

<h2>Upcoming Events</h2>

<ul>
{events.map((event)=>(
<li key={event.id}>
{event.name} - {event.date}
</li>
))}
</ul>

</div>
);
}

export default EventList;