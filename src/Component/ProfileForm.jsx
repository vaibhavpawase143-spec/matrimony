import React, { useState, useMemo } from "react";

export default function ProfileForm() {
  const initialState = {
    name: "",
    dob: "",
    age: "",
    gender: "",
    height: "",
    weight: "",
    religion: "",
    caste: "",
    maritalStatus: "",
    father: "",
    mother: "",
    grandFather: "",
    grandMother: "",
    familyType: "",
    qualification: "",
    college: "",
    profession: "",
    income: "",
    smoking: "",
    drinking: "",
    Diet: "",
    partner: ""
  };

  const [form, setForm] = useState(initialState);

  const todayDate = useMemo(() => new Date().toISOString().split("T")[0], []);

  const heightOptions = [];
  for (let i = 144; i <= 170; i++) {
    heightOptions.push(i);
  }

  const qualificationOptions = [
    "High School", "Diploma", "B.A", "B.Sc", "B.Com", "B.Tech / B.E",
    "BCA", "BBA", "M.A", "M.Sc", "M.Com", "M.Tech / M.E", "MBA",
    "MCA", "MBBS", "MD", "PhD", "Other"
  ];

  const calculateAge = (dob) => {
    if (!dob) return "";
    const birthDate = new Date(dob);
    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    const m = today.getMonth() - birthDate.getMonth();
    if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
      age--;
    }
    return age >= 0 ? age : "";
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === "dob") {
      const age = calculateAge(value);
      setForm({ ...form, dob: value, age });
    } else {
      setForm({ ...form, [name]: value });
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Saving Data:", form);
    alert("Profile Saved Successfully!");
    setForm(initialState);
  };

  return (
    <div className="wrapper">
      <span className="bg-heart h-top-left">❤</span>
      <span className="bg-heart h-top-right">❤</span>
      <span className="bg-heart h-bottom-left">❤</span>

      <div className="container">
        <div className="header">
          <div className="brand">
            <span className="heart-icon">❤</span>
            <span className="brand-name">GathBandhan</span>
          </div>
          <p className="description">Provide your details to find a compatible partner</p>
        </div>

        <form onSubmit={handleSubmit}>
          {/* --- SECTION: PERSONAL --- */}
          <div className="section">
            <h2 className="section-title">Personal Details</h2>
            <div className="form-grid">
              <div className="input-box">
                <label>Full Name </label>
                <input name="name" value={form.name} placeholder="Full Name" onChange={handleChange} required />
              </div>
              <div className="input-box">
                <label>Date of Birth </label>
                <input
                  type="date"
                  name="dob"
                  value={form.dob}
                  max={todayDate}
                  onChange={handleChange}
                  required
                  className="calendar-input"
                />
              </div>
              <div className="input-box">
                <label>Age</label>
                <input name="age" value={form.age} readOnly className="read-only" placeholder="Age" />
              </div>
              <div className="input-box">
                <label>Gender </label>
                <select name="gender" value={form.gender} onChange={handleChange} required>
                  <option value="" disabled>Select Gender</option>
                  <option value="Male">Male</option>
                  <option value="Female">Female</option>
                </select>
              </div>
              <div className="input-box">
                <label>Height </label>
                <select name="height" value={form.height} onChange={handleChange} required>
                  <option value="" disabled>Select Height</option>
                  {heightOptions.map(h => <option key={h} value={h}>{h} cm</option>)}
                </select>
              </div>
              <div className="input-box">
                <label>Weight</label>
                <input name="weight" value={form.weight} placeholder="Weight " onChange={handleChange} />
              </div>
              <div className="input-box">
                <label>Religion </label>
                <input name="religion" value={form.religion} placeholder="Religion" onChange={handleChange} required />
              </div>
              <div className="input-box">
                <label>Caste </label>
                <input name="caste" value={form.caste} placeholder="Caste" onChange={handleChange} required />
              </div>
              <div className="input-box">
                <label>Marital Status </label>
                <select name="maritalStatus" value={form.maritalStatus} onChange={handleChange} required>
                  <option value="" disabled>Select Marital Status</option>
                  <option value="Single">Single</option>
                  <option value="Divorced">Divorced</option>
                  <option value="Widowed">Widowed</option>
                </select>
              </div>
            </div>
          </div>

          {/* --- SECTION: FAMILY --- */}
          <div className="section">
            <h2 className="section-title">Family Details</h2>
            <div className="form-grid">
              <div className="input-box">
                <label>Father's Name </label>
                <input name="father" value={form.father} placeholder="Father's Name" onChange={handleChange} required />
              </div>
              <div className="input-box">
                <label>Mother's Name </label>
                <input name="mother" value={form.mother} placeholder="Mother's Name" onChange={handleChange} required />
              </div>
              <div className="input-box">
                <label>GrandFather Name </label>
                <input name="grandFather" value={form.grandFather} placeholder="GrandFather Name" onChange={handleChange} required />
              </div>
              <div className="input-box">
                <label>GrandMother Name </label>
                <input name="grandMother" value={form.grandMother} placeholder="GrandMother Name" onChange={handleChange} required />
              </div>
              <div className="input-box">
                <label>Sibling </label>
                <input name="sibling" value={form.sibling} placeholder="Sibling Names" onChange={handleChange} required /> 
              </div>
              <div className="input-box">
                <label>Family Type </label>
                <select name="familyType" value={form.familyType} onChange={handleChange} required>
                  <option value="" disabled>Select Type</option>
                  <option value="Joint">Joint</option>
                  <option value="Nuclear">Nuclear</option>
                </select>
              </div>
            </div>
          </div>

          {/* --- SECTION: EDUCATION --- */}
          <div className="section">
            <h2 className="section-title">Education & Career</h2>
            <div className="form-grid">
              <div className="input-box">
                <label>Qualification </label>
                <select name="qualification" value={form.qualification} onChange={handleChange} required>
                  <option value="" disabled>Select Qualification</option>
                  {qualificationOptions.map(q => <option key={q} value={q}>{q}</option>)}
                </select>
              </div>
              <div className="input-box">
                <label>College *</label>
                <input name="college" value={form.college} placeholder="College Name" onChange={handleChange} required />
              </div>
              <div className="input-box">
                <label>Profession *</label>
                <input name="profession" value={form.profession} placeholder="Job Title" onChange={handleChange} required />
              </div>
              <div className="input-box">
                <label>Income *</label>
                <input name="income" value={form.income} placeholder="Annual Income" onChange={handleChange} required />
              </div>
            </div>
          </div>

          {/* --- SECTION: LIFESTYLE --- */}
          <div className="section">
            <h2 className="section-title">Lifestyle</h2>
            <div className="form-grid">
              <div className="input-box">
                <label>Smoking *</label>
                <select name="smoking" value={form.smoking} onChange={handleChange} required>
                  <option value="" disabled>Select</option>
                  <option value="No">No</option>
                  <option value="Yes">Yes</option>
                </select>
              </div>
              <div className="input-box">
                <label>Drinking *</label>
                <select name="drinking" value={form.drinking} onChange={handleChange} required>
                  <option value="" disabled>Select</option>
                  <option value="No">No</option>
                  <option value="Occasionally">Occasionally</option>
                  <option value="Regularly">Regularly</option>
                </select>
              </div>
              <div className="input-box">
                <label>Diet *</label>
                <select name="Diet" value={form.Diet} onChange={handleChange} required>
                  <option value="" disabled>Select</option>
                  <option value="Vegetarian">Vegetarian</option>
                  <option value="Non-vegetarian">Non-vegetarian</option>
                </select>
              </div>
            </div>
          </div>

          {/* --- SECTION: PREFERENCE --- */}
          <div className="section">
            <h2 className="section-title">Partner Preference</h2>
            <textarea
              name="partner"
              value={form.partner}
              placeholder="Tell us about your expectations..."
              onChange={handleChange}
              required
            ></textarea>
          </div>

          <button type="submit" className="btn">Save Profile</button>
        </form>
      </div>

      <style>{`
        * { box-sizing: border-box; margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }

        .wrapper {
          min-height: 100vh;
          width: 100%;
          background: radial-gradient(circle at center, #9333ea 0%, #6b21a8 100%);
          display: flex; 
          justify-content: center; 
          align-items: flex-start;
          padding: 60px 15px;
          position: relative;
          overflow: hidden;
        }

        .bg-heart {
          position: absolute;
          color: rgba(255, 255, 255, 0.15);
          font-size: 32px;
          user-select: none;
          pointer-events: none;
        }
        .h-top-left { top: 10%; left: 8%; transform: rotate(-15deg); }
        .h-top-right { top: 22%; right: 12%; font-size: 24px; transform: rotate(10deg); }
        .h-bottom-left { bottom: 15%; left: 20%; transform: rotate(5deg); }

        .container {
          width: 100%; max-width: 850px;
          background: #ffffff; 
          padding: 40px; 
          border-radius: 16px;
          box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
          position: relative;
          z-index: 10;
          text-align: left; /* Global left alignment */
        }

        .header { text-align: center; margin-bottom: 35px; }
        .brand { display: flex; align-items: center; justify-content: center; gap: 10px; margin-bottom: 5px; }
        .heart-icon { color: #9d174d; font-size: clamp(24px, 5vw, 32px); }
        .brand-name { font-size: clamp(28px, 6vw, 36px); font-weight: 800; color: #1a1a1a; letter-spacing: -1px; }
        .description { color: #666; font-size: 14px; margin-top: 8px; }

        .section { margin-bottom: 35px; text-align: left; }
        .section-title {
          font-size: 13px; color: #9d174d; font-weight: 700;
          text-transform: uppercase; border-bottom: 2px solid #fce4ec;
          padding-bottom: 4px; margin-bottom: 20px; letter-spacing: 1px;
          text-align: left; /* Forces title to the left */
        }

        .form-grid {
          display: grid;
          grid-template-columns: repeat(2, 1fr); /* Two columns on desktop */
          gap: 20px;
          justify-items: start;
        }

        .input-box { display: flex; flex-direction: column; gap: 6px; width: 100%; text-align: left; }
        .input-box label { font-size: 13px; color: #4b5563; font-weight: 600; text-align: left; }

        input, select, textarea {
          width: 100%; height: 48px;
          padding: 0 14px; border: 1px solid #d1d5db;
          border-radius: 8px; font-size: 15px;
          background-color: #fefefe; color: #111827;
          outline: none; transition: all 0.2s;
        }

        input:focus, select:focus, textarea:focus {
          border-color: #9d174d;
          box-shadow: 0 0 0 3px rgba(157, 23, 77, 0.1);
        }

        textarea { height: 100px; padding: 12px; resize: vertical; grid-column: span 2; }
        .read-only { background: #f3f4f6; color: #6b7280; cursor: not-allowed; }

        .calendar-input::-webkit-calendar-picker-indicator {
          cursor: pointer;
          filter: invert(15%) sepia(95%) saturate(3928%) hue-rotate(325deg) brightness(88%) contrast(92%);
        }

        .btn {
          width: 100%; padding: 18px; background: #9d174d;
          color: white; border: none; border-radius: 10px;
          font-size: 18px; font-weight: 700; cursor: pointer;
          margin-top: 10px; transition: background 0.2s, transform 0.1s;
        }
        .btn:hover { background: #83103f; }
        .btn:active { transform: scale(0.99); }

        @media (max-width: 650px) {
          .wrapper { padding: 0; }
          .container { padding: 30px 20px; border-radius: 0; }
          .form-grid { grid-template-columns: 1fr; }
          textarea { grid-column: span 1; }
        }
      `}</style>
    </div>
  );
}