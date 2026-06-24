import {
Heart,
Send,
MessageSquare,
Bookmark,
Eye,
Users
} from 'lucide-react';
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { subscriptionAPI } from "@/services/api";
const DashboardStats = ({ stats = {} }) => {

const navigate = useNavigate();
const [showUpgradePopup, setShowUpgradePopup] = useState(false);
const defaultStats = {

totalMatches:
stats.totalMatches || 0,

interestsSent:
stats.interestsSent || 0,

interestsReceived:
stats.interestsReceived || 0,

shortlists:
stats.shortlists || 0,

profileViews:
stats.profileViews || 0,
likesReceived:
stats.likesReceived || 0,

messages:
stats.messages || 0,

};
const statItems = [

{

label:'Total Matches',

value:
defaultStats.totalMatches,

icon:Users,

color:
'bg-blue-100 dark:bg-blue-900/30',

textColor:
'text-blue-600 dark:text-blue-400'

},

{

label:'Interests Sent',

value:
defaultStats.interestsSent,

icon:Send,

color:
'bg-purple-100 dark:bg-purple-900/30',

textColor:
'text-purple-600 dark:text-purple-400',

route:'/sent-interests'

},



{

label:'Interests Received',

route:'/received-interests',

value:
defaultStats.interestsReceived,

icon:Heart,

color:
'bg-red-100 dark:bg-red-900/30',

textColor:
'text-red-600 dark:text-red-400'

},
{

label:'Shortlists',

value:
defaultStats.shortlists,

icon:Bookmark,

color:
'bg-amber-100 dark:bg-amber-900/30',

textColor:
'text-amber-600 dark:text-amber-400',

route:'/shortlists'

},
{
label:'Profile Visitors',

value:
defaultStats.profileViews,

icon:Eye,

color:
'bg-green-100 dark:bg-green-900/30',

textColor:
'text-green-600 dark:text-green-400',

route:'/profile-visitors'
},
{
label:'Likes Received',

value: defaultStats.likesReceived || 0,

icon: Heart,

color:'bg-pink-100 dark:bg-pink-900/30',

textColor:'text-pink-600 dark:text-pink-400',

route:'/likes'
},
{

label:'Messages',

value:
defaultStats.messages,

icon:MessageSquare,

color:
'bg-cyan-100 dark:bg-cyan-900/30',

textColor:
'text-cyan-600 dark:text-cyan-400',
 route: '/messages'
}

];

return(
<>
<div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">

{

statItems.map((stat,index)=>{

const Icon = stat.icon;

return(

<div

key={index}

onClick={async () => {

    if (!stat.route) return;

    if (stat.route === "/messages") {

        try {

            const subscription =
                await subscriptionAPI.getMySubscription();

            if (subscription?.isActive) {

                navigate("/messages");

           } else {

               setShowUpgradePopup(true);

           }

           } catch {

               setShowUpgradePopup(true);

           }
        return;

    }

    navigate(stat.route);

}}
className={`

${stat.color}

rounded-lg

p-4

border

border-transparent

hover:border-primary/20

transition-all

duration-200

${

stat.route

?

'cursor-pointer hover:scale-[1.02]'

:

''

}

`}

>

<div className="flex items-center justify-between mb-2">

<p className="text-sm font-medium text-muted-foreground">

{stat.label}

</p>

<Icon className={`h-5 w-5 ${stat.textColor}`}/>

</div>

<p className={`text-3xl font-bold ${stat.textColor}`}>

{stat.value}

</p>

</div>

);

})

}

</div>

{
showUpgradePopup && (

<div className="fixed inset-0 bg-black/50 flex items-center justify-center z-[999999]">

    <div className="bg-white rounded-3xl p-8 w-[420px] text-center">

        <div className="text-6xl mb-4">
            👑
        </div>

        <h2 className="text-2xl font-bold mb-3">
            Premium Required
        </h2>

        <p className="text-gray-600 mb-6">
            Chat is available only for Premium members.
        </p>

        <div className="flex justify-center gap-3">

            <button
                onClick={() => {
                    setShowUpgradePopup(false);
                    navigate("/home");
                }}
                className="px-5 py-2 rounded-xl bg-gray-200"
            >
                Home
            </button>

            <button
                onClick={() => {
                    setShowUpgradePopup(false);
                    navigate("/upgrade");
                }}
                className="px-5 py-2 rounded-xl bg-pink-600 text-white"
            >
                Upgrade Premium
            </button>

        </div>

    </div>

</div>

)
}
</>
);

};

export default DashboardStats;