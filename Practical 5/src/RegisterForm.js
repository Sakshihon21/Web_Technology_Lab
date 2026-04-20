import React, { useState } from "react";

function RegisterForm() {

  const [name,setName] = useState("");
  const [email,setEmail] = useState("");
  const [phone,setPhone] = useState("");
  const [event,setEvent] = useState("");
  const [payment,setPayment] = useState("");

  const [registrations,setRegistrations] = useState([]);
  const [editIndex,setEditIndex] = useState(null);

  const handleSubmit = (e) => {

    e.preventDefault();

    const newData = {
      name,
      email,
      phone,
      event,
      payment
    };

    if(editIndex !== null){

      const updated = [...registrations];
      updated[editIndex] = newData;

      setRegistrations(updated);
      setEditIndex(null);

    }
    else{

      setRegistrations([...registrations,newData]);

    }

    setName("");
    setEmail("");
    setPhone("");
    setEvent("");
    setPayment("");
  };

  const handleDelete = (index) => {

    const updated = registrations.filter((_,i)=> i !== index);
    setRegistrations(updated);

  };

  const handleEdit = (index) => {

    const data = registrations[index];

    setName(data.name);
    setEmail(data.email);
    setPhone(data.phone);
    setEvent(data.event);
    setPayment(data.payment);

    setEditIndex(index);

  };

  return (

    <div>

      <h2>Event Registration</h2>

      <form onSubmit={handleSubmit}>

        <input
        type="text"
        placeholder="Enter Name"
        value={name}
        onChange={(e)=>setName(e.target.value)}
        required
        />

        <input
        type="email"
        placeholder="Enter Email"
        value={email}
        onChange={(e)=>setEmail(e.target.value)}
        required
        />

        <input
        type="text"
        placeholder="Phone Number"
        value={phone}
        onChange={(e)=>setPhone(e.target.value)}
        required
        />

        <select
        value={event}
        onChange={(e)=>setEvent(e.target.value)}
        required
        >

          <option value="">Select Event</option>
          <option>AI Workshop</option>
          <option>Hackathon</option>
          <option>Robotics Club</option>

        </select>

        <div className="payment">

          <h4>Payment Method</h4>

          <label>

            <input
            type="radio"
            value="Cash"
            checked={payment==="Cash"}
            onChange={(e)=>setPayment(e.target.value)}
            />

            Cash

          </label>

          <label style={{marginLeft:"20px"}}>

            <input
            type="radio"
            value="Online"
            checked={payment==="Online"}
            onChange={(e)=>setPayment(e.target.value)}
            />

            Online

          </label>

        </div>

        <button type="submit">

          {editIndex !== null ? "Update Registration" : "Register"}

        </button>

      </form>

      <hr/>

      <h2>Registered Students</h2>

      <table>

        <thead>

          <tr>

            <th>Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Event</th>
            <th>Payment</th>
            <th>Actions</th>

          </tr>

        </thead>

        <tbody>

          {registrations.map((data,index)=>(

            <tr key={index}>

              <td>{data.name}</td>
              <td>{data.email}</td>
              <td>{data.phone}</td>
              <td>{data.event}</td>
              <td>{data.payment}</td>

              <td>

                <button
                className="action-btn edit-btn"
                onClick={()=>handleEdit(index)}
                >
                Edit
                </button>

                <button
                className="action-btn delete-btn"
                onClick={()=>handleDelete(index)}
                >
                Delete
                </button>

              </td>

            </tr>

          ))}

        </tbody>

      </table>

    </div>

  );

}

export default RegisterForm;