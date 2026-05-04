import { Heart } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import { useAuth } from "@/hooks/useAuth";
import { useToast } from "@/components/Toast";
import { useLoading } from "@/hooks/useLoading";
import MatrimonySelect from "@/components/MatrimonySelect";
import { useMatrimonyOptions } from "@/hooks/useMatrimonyOptions";
import { authAPI, masterDataAPI } from "@/services/api";
import { useLanguage } from "@/context/LanguageContext.jsx";

const Register = () => {
  const navigate = useNavigate();
  const { login } = useAuth();
  const { success, error } = useToast();
  const { startLoading, stopLoading } = useLoading();
  const { getOptions, addCustomOption } = useMatrimonyOptions();
  const { t } = useLanguage();
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [gender, setGender] = useState("");
  const [dob, setDob] = useState("");
  const [religion, setReligion] = useState("");
  const [religionId, setReligionId] = useState("");
  const [errors, setErrors] = useState({});

  const validateForm = () => {
    const errs = {};
    
    // Name validation
    if (!firstName.trim()) {
      errs.firstName = t.register.errors.firstNameRequired;
    } else if (firstName.trim().length < 2) {
      errs.firstName = t.register.errors.firstNameMin;
    }
    
    if (!email.trim()) {
      errs.email = t.register.errors.emailRequired;
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      errs.email = t.register.errors.emailInvalid;
    }
    
    if (!phone.trim()) {
      errs.phone = t.register.errors.phoneRequired;
    } else if (!/^\d{10,15}$/.test(phone.replace(/\D/g, ''))) {
      errs.phone = t.register.errors.phoneInvalid;
    }
    
    if (!password.trim()) {
      errs.password = t.register.errors.passwordRequired;
    } else if (password.length < 6) {
      errs.password = t.register.errors.passwordMin;
    }
    
    if (!confirmPassword.trim()) {
      errs.confirmPassword = t.register.errors.confirmPasswordRequired;
    } else if (password !== confirmPassword) {
      errs.confirmPassword = t.register.errors.passwordsMismatch;
    }
    
    if (!gender) {
      errs.gender = t.register.errors.genderRequired;
    }
    
    if (!dob) {
      errs.dob = t.register.errors.dobRequired;
    } else {
      const birthDate = new Date(dob);
      const today = new Date();
      const age = today.getFullYear() - birthDate.getFullYear();
      const monthDiff = today.getMonth() - birthDate.getMonth();
      
      if (age < 18 || (age === 18 && monthDiff < 0)) {
        errs.dob = t.register.errors.dobAge;
      }
    }
    
    if (!religion) {
      errs.religion = t.register.errors.religionRequired;
    }
    
    return errs;
  };

  const handleRegister = async () => {
    const errs = validateForm();
    setErrors(errs);
    
    if (Object.keys(errs).length === 0) {
      startLoading(t.register.messages.creatingAccount);
      
      try {
        // TODO: connect backend API
        const registrationData = {
          firstName: firstName.trim(),
          lastName: lastName.trim(),
          email: email.trim().toLowerCase(),
          phone: phone.replace(/\D/g, ''),
          password: password,
          gender: gender,
          dob: dob,
          religion_id: religionId || null,
        };

        const response = await authAPI.register(registrationData);

        if (response.token) {
          localStorage.setItem('token', response.token);
        }
        
        const userName = response.user?.first_name || `${firstName} ${lastName}`.trim() || firstName;
        login(userName);

        success(t.register.messages.registerSuccess);
        stopLoading();

        // Fixed redirect to valid route
        navigate("/home");
        
      } catch (err) {
        stopLoading();
        const errorMessage = err.message || t.register.messages.registerFailed;
        error(errorMessage);
        
        if (err.errors) {
          setErrors(err.errors);
        }
      }
    } else {
      error(t.register.messages.fillAllFields);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center relative overflow-hidden" style={{ background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))" }}>
      <Heart className="absolute top-12 left-[10%] h-5 w-5 text-pink-soft fill-pink-soft opacity-40 animate-float-heart" />
      <Heart className="absolute top-24 right-[20%] h-4 w-4 text-pink-soft fill-pink-soft opacity-30 animate-float-heart [animation-delay:1s]" />
      <Heart className="absolute bottom-20 left-[30%] h-6 w-6 text-pink-soft fill-pink-soft opacity-30 animate-float-heart [animation-delay:0.5s]" />

      <div className="bg-card rounded-2xl shadow-2xl p-8 w-full max-w-md mx-4 relative z-10">
        <div className="text-center mb-6">
          <div className="flex items-center justify-center gap-2 mb-2">
            <Heart className="h-7 w-7 text-primary fill-primary" />
            <span className="text-2xl font-display font-bold text-foreground">Gathbandhan</span>
          </div>
          <p className="text-muted-foreground text-sm">{t.register.subtitle}</p>
        </div>

        <div className="space-y-4">
          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="text-xs font-medium text-foreground mb-1 block">{t.register.firstName}</label>
              <input value={firstName} onChange={(e) => setFirstName(e.target.value)} className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" placeholder={t.register.firstName} />
              {errors.firstName && <p className="text-xs text-destructive mt-1">{errors.firstName}</p>}
            </div>
            <div>
              <label className="text-xs font-medium text-foreground mb-1 block">{t.register.lastName}</label>
              <input value={lastName} onChange={(e) => setLastName(e.target.value)} className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" placeholder={t.register.lastName} />
            </div>
          </div>

          <div>
            <label className="text-xs font-medium text-foreground mb-1 block">{t.register.email}</label>
            <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" placeholder={t.register.email} />
            {errors.email && <p className="text-xs text-destructive mt-1">{errors.email}</p>}
          </div>

          <div>
            <label className="text-xs font-medium text-foreground mb-1 block">{t.register.phone}</label>
            <input type="tel" value={phone} onChange={(e) => setPhone(e.target.value)} className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" placeholder="+91 9876543210" />
            {errors.phone && <p className="text-xs text-destructive mt-1">{errors.phone}</p>}
          </div>

          <div>
            <label className="text-xs font-medium text-foreground mb-1 block">{t.register.password}</label>
            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" placeholder={t.register.password} />
            {errors.password && <p className="text-xs text-destructive mt-1">{errors.password}</p>}
          </div>

          <div>
            <label className="text-xs font-medium text-foreground mb-1 block">{t.register.confirmPassword}</label>
            <input type="password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" placeholder={t.register.confirmPassword} />
            {errors.confirmPassword && <p className="text-xs text-destructive mt-1">{errors.confirmPassword}</p>}
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="text-xs font-medium text-foreground mb-1 block">{t.register.gender}</label>
              <select value={gender} onChange={(e) => setGender(e.target.value)} className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary">
                <option value="">{t.register.selectGender}</option>
                <option value="Male">{t.register.genderOptions.male}</option>
                <option value="Female">{t.register.genderOptions.female}</option>
              </select>
              {errors.gender && <p className="text-xs text-destructive mt-1">{errors.gender}</p>}
            </div>
            <div>
              <label className="text-xs font-medium text-foreground mb-1 block">{t.register.dob}</label>
              <input type="date" value={dob} onChange={(e) => setDob(e.target.value)} className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" />
              {errors.dob && <p className="text-xs text-destructive mt-1">{errors.dob}</p>}
            </div>
          </div>

          <div>
            <label className="text-xs font-medium text-foreground mb-1 block">{t.register.religion}</label>
            <MatrimonySelect
              options={getOptions('religion')}
              value={religion}
              onChange={(value) => {
                setReligion(value);
              }}
              placeholder={t.register.selectReligion}
              fieldType="religion"
              onAddCustom={addCustomOption}
            />
            {errors.religion && <p className="text-xs text-destructive mt-1">{errors.religion}</p>}
          </div>

          <button onClick={handleRegister} className="w-full bg-primary hover:bg-primary/90 text-primary-foreground font-semibold py-2.5 rounded-lg text-sm transition-colors">
            {t.register.button}
          </button>

          <p className="text-center text-xs text-muted-foreground">
            {t.register.haveAccount}{" "}
            <Link to="/login" className="text-primary font-semibold hover:underline">{t.register.loginLink}</Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Register;
