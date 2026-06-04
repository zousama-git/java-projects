# HealthCoach 🏃 — Smart Console Health App
A feature-rich, interactive Java console application that goes far beyond a basic BMI calculator. It's a personalized health coach experience with real metrics, visual dashboards, progress tracking, and AI-style coaching.

## Features
### 🎨 Visual Terminal Experience
- ANSI color-coded output (green = healthy, yellow = caution, red = risk)
- ASCII BMI scale with live pointer showing your position
- Progress bars for at-a-glance metrics
- Styled banners, section headers, and menu layouts
- Step indicators [1/5], [2/5]... guiding you through inputs

### 👤 User System
- **Login** with existing accounts
- **Register** a new account with profile setup
- **Guest mode** for quick checks without an account
- 5 seeded accounts ready to use (see below)

### 📊 Full Health Dashboard (not just BMI!)
| Metric | Method |
|--------|--------|
| BMI | Standard formula (kg/m²) |
| BMI Category | WHO classification (7 tiers) |
| Estimated Body Fat % | Deurenberg formula |
| BMR | Mifflin-St Jeor equation |
| TDEE | BMR × activity multiplier |
| Calorie target | Adjusted for your goal |
| Waist-to-Height Ratio | Optional, with risk categories |
| Ideal weight range | BMI 18.5–24.9 back-calculated |

### 🧠 Smart Personalization
- Profile stores: age, gender, activity level, goal (lose/maintain/gain)
- All metrics (TDEE, calorie target, tips) adapt to your profile
- Logged-in users skip re-entering stored data

### 💬 Coach Personality
- Narrative explanation of what your BMI *means in real life*
- Personalized opening line based on your BMI + goal combination
- 4 tailored tips every session (goal-specific + activity-specific)
- Milestone celebrations for streaks and BMI improvements

### 📈 Progress Tracker
- History of every check-in (month, weight, BMI, trend arrow)
- Check-in streak counter + total check-in count
- Trend indicators: ↓ improving, ↑ rising, → stable
- Milestone messages: "You dropped 2.2 BMI points since you started!"

### 🔮 Future Projections
- Simulate BMI at −5, −3, −1, 0, +1, +3, +5 kg
- Time-to-goal estimate (at 0.5 kg/week pace)
- "Future You" projection based on actual historical pace

### 🔄 Unit Toggle
- Enter all measurements in metric
- View results in either metric (kg, cm) or imperial (lbs, ft/in)


## Accounts
| Username | Password   | Profile                        |
|----------|------------|--------------------------------|
| alice    | pass123    | 28F, moderate activity, goal: lose, 3-entry history |
| bob      | qwerty     | 35M, active, goal: gain muscle, 3-entry history |
| charlie  | hello99    | 42M, sedentary, goal: lose, 3-entry history |
| diana    | sunshine   | No profile set up yet |
| eve      | qwerty123  | No profile set up yet |
