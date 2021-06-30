import sys
def numtohebwords(number):
    y=int(number)
    x,tyu=str(y),print("\n")
    if int(x)>10**100 or int(x)<0:sys.exit("Range Is From Zero To One Googol!")
    def num1wrd(x,w={0:"",1:"אחד",2:"שתים",3:"שלוש",4:"ארבע",5:"חמש",6:"שש",7:"שבע",8:"שמונה",9:"תשע"},f={2:"עשרים ו",3:"שלושים ו",4:"ארבעים ו",5:"חמשים ו",6:"שישים ו",7:"שבעים",8:"שמונים",9:"תשעים"},t={11:"אחד עשרה",12:"שנין עשרה ",13:"שלוש עשרה",14:"אחד עשרה",15:"ארבע עשרה",16:"שש עשרה",17:"שבע עשרה",18:"שמונה עשרה",19:"תשעה עשרה"}):
        if len(x)==1:return(w[int(x)])
        elif len(x)==2 and x[0]=="0" and x[1]=="0":return("")
        elif len(x)==2 and x[0]=="0":return (w[int(x[1])])
        elif len(x)==2:
            if int(x) in range(11,20):return(t[int(x)])
            elif int(x[1])==0:
                if int(x)==10:return("עשר")
                else:return(f[int(x[0])])
            else:return(f[int(x[0])]+w[int(x[1])])
    def hun_num(x,w1={0:"",1:"מאה",2:"מתאים",3:"שלוש מאות",4:"ארבע מאות",5:"חמש מאות",6:"שש מאות",7:"שבע מאות",8:"שמונה מאות",9:"תשע מאות"}):
        if len(x)==3 and x[0]!="0" and x[1]=="0" and x[2]=="0": return(w1[int(x[0])])
        elif len(x)==3 and x[0]!="0":
            a1=(x[1]+x[2])
            return(w1[int(x[0])]+" ו"+num1wrd(a1))
        elif len(x)==3 and x[0]=="0": return(num1wrd(x[1]+x[2])+" ")
        else: return(num1wrd(x))
    def seg3(s,out = []):
        while len(s):
            out.insert(0, s[-3:])
            s = s[:-3]
        return out
    def q(x):
        if x=="000":return(0)
        elif x=="00":return(0)
        elif x=="0":return(0)
        else:return(1)
    aa,v={0:"",1:"אלף",2:"מיליון",3:"ביליון",4:"טריליון",5:"קודריליון",6:"קווינטריליון",7:"סקסטיליון",8:"ספטיליון",9:"אוקטיליון",10:"נוניליון",11:"דסיליון",12:"אנדסיליון",13:"דודסיליון",15:"טרדסאליון",16:"קואדטורדסאליון",17:"קווינדאסליון",18:"סקסדסאליון",19:"אוקטודסאליון",20:"נובאמדסאליון",21:"ויג'יטיליון",14:"סנטיליון",22:"סנטיליון",23:"Duovigintillion",24:"סֶפּטיליון"},seg3(x)
    if int(x)==10**100: s="One Googol"
    elif int(x)==0: s="אפס"
    else:
        s1=""
        for i in range(len(v)):s1=s1+(hun_num(v[i]))+(" "+aa[len(v)-1-i]+", ")*q(v[i])
        s=s1[:len(s1)-3]
        if s.startswith("אחד"):
           s = s[3:]
        return s + "."



