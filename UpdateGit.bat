
set /p Input=Write What You Update To The Project: 
git pull origin master
git add .
git commit -m "%Input%"
git push origin main
