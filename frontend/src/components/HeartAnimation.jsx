import { AnimatePresence,motion }

from "framer-motion";

const HeartAnimation=({

show,

onComplete

})=>{

return(

<AnimatePresence>

{

show && (

<motion.div

className="

absolute

inset-0

flex

items-center

justify-center

pointer-events-none

z-50

"

initial={{opacity:0}}

animate={{opacity:1}}

exit={{opacity:0}}

>

<motion.div

initial={{

scale:0,

opacity:0

}}

animate={{

scale:[0,1.3,1],

opacity:[0,1,0],

y:[0,-20,-90]

}}

transition={{

duration:.8

}}

onAnimationComplete={

onComplete

}

className="

text-[110px]

drop-shadow-lg

"

>

❤️

</motion.div>

</motion.div>

)

}

</AnimatePresence>

);

};

export default HeartAnimation;