package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

// --- CORE QUESTIONS ---
data class Question(
    val id: Int,
    val subject: String,  // Physics, Chemistry, Biology
    val topic: String,
    val text: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val solutionText: String,
    val difficulty: Int = 3 // 3 is Hardest, 2 is Medium, 1 is Easy
)

// --- PRE-SEEDED QUESTION POOL ---
object QuestionPool {
    val questions = listOf(
        Question(
            id = 1,
            subject = "Physics",
            topic = "Rotational Mechanics & Power",
            text = "A particle of mass m is moving in a circular path of constant radius r such that its centripetal acceleration is varying with time t as ac = k^2 r t^2. The power delivered to the particle by the forces acting on it is:",
            options = listOf("2\u03C0 mk^2 r^2 t", "mk^2 r^2 t", "1/3 mk^4 r^2 t^5", "0"),
            correctOptionIndex = 1,
            solutionText = "Detailed AI Solution:\n1. Centripetal acceleration is given by:\n   ac = v^2 / r = k^2 * r * t^2\n2. From this, we can solve for velocity v:\n   v^2 = k^2 * r^2 * t^2  =>  v = k * r * t\n3. Tangential acceleration (at) is the rate of change of speed:\n   at = dv / dt = d(k * r * t) / dt = k * r\n4. The forward tangential force acting on the particle is:\n   Ft = m * at = m * k * r\n5. Power delivered is work done per unit time, given by the product of tangential force and tangential velocity:\n   P = Ft * v = (m * k * r) * (k * r * t) = m * k^2 * r^2 * t.\n\nThus, the correct option is Option 2: mk^2 r^2 t."
        ),
        Question(
            id = 2,
            subject = "Physics",
            topic = "Electrostatics",
            text = "Two point charges +q and -q are held fixed at (-d, 0) and (d, 0) respectively. A third charge 2q is projected from infinity with an initial speed v along the perpendicular bisector (y-axis). The minimum initial velocity v so that the charge reaches the origin is:",
            options = listOf(
                "sqrt(q^2 / (\u03C0 \u03B50 m d))",
                "sqrt(2 q^2 / (\u03C0 \u03B50 m d))",
                "sqrt(q^2 / (2 \u03C0 \u03B50 m d))",
                "0"
            ),
            correctOptionIndex = 3,
            solutionText = "Detailed AI Solution:\n1. Along the perpendicular bisector (y-axis), any point P(0, y) is equidistant from both +q at (-d,0) and -q at (d,0).\n2. The electric potential (V) at any point on the y-axis due to these two equal and opposite charges is:\n   V = k*q / sqrt(d^2 + y^2) + k*(-q) / sqrt(d^2 + y^2) = 0.\n3. Since the electric potential is zero everywhere along the perpendicular bisector, including at infinity and at the origin (0,0), there is no electric work required to move the charge 2q along this axis.\n4. Hence, no potential energy barrier exists, and the minimum initial velocity required to reach the origin is exactly 0."
        ),
        Question(
            id = 3,
            subject = "Chemistry",
            topic = "Organic Chemistry (Elimination)",
            text = "Identify the major organic product formed when 2-bromobutane is heated with alcoholic KOH under high temperature:",
            options = listOf("1-butene", "trans-2-butene", "cis-2-butene", "2-butanol"),
            correctOptionIndex = 1,
            solutionText = "Detailed AI Solution:\n1. The reaction of an alkyl halide (2-bromobutane) with a strong, hot base like alcoholic KOH undergoes an E2 (Elimination Bimolecular) pathway.\n2. Elimination follows Saytzeff's Rule, which states that the highly substituted, highly stable alkene will be the major product (due to hyperconjugation and resonance stability).\n3. Here, elimination can yield either 1-butene or 2-butene. 2-Butene is more stable because it has 6 alpha-hydrogens compared to 2 alpha-hydrogens in 1-butene.\n4. Between the two stereoisomers of 2-butene, trans-2-butene is significantly more stable than cis-2-butene due to reduced steric hindrance between the methyl groups.\n5. Therefore, trans-2-butene is the primary major product, comprising around 60-70% of the yield."
        ),
        Question(
            id = 4,
            subject = "Chemistry",
            topic = "Coordination Compounds",
            text = "The hybridization and magnetic behavior of the complex [Co(NH3)6]^3+ is (Atomic number of Co = 27):",
            options = listOf(
                "sp3d2 and paramagnetic",
                "d2sp3 and diamagnetic",
                "d2sp3 and paramagnetic",
                "sp3d2 and diamagnetic"
            ),
            correctOptionIndex = 1,
            solutionText = "Detailed AI Solution:\n1. Cobalt in [Co(NH3)6]^3+ is in the +3 oxidation state. Co has atomic number 27, so Co = [Ar] 3d7 4s2. Co^3+ has configuration [Ar] 3d6.\n2. Ammonia (NH3) acts as a strong field ligand (SFL) in this cobalt complex. According to Crystal Field Theory, strong field ligands cause pairing of d-electrons.\n3. The 6 electrons in the 3d subshell pair up completely in the three t2g orbitals (leaving the two eg orbitals vacant):\n   t2g^6 eg^0. There are 0 unpaired electrons.\n4. Since there are 0 unpaired electrons, the complex is diamagnetic.\n5. The two vacant 3d (eg) orbitals, along with one 4s and three 4p orbitals, participate in hybridization, yielding d2sp3 (inner orbital complex) hybridization.\n\nThus, the configuration is d2sp3 and diamagnetic."
        ),
        Question(
            id = 5,
            subject = "Biology",
            topic = "Genetics & Inheritance",
            text = "In a typical dihybrid cross, if two plants heterozygous for both round seeds (Rr) and yellow cotyledons (Yy) are crossed, what fraction of the offspring is expected to have wrinkled green seeds (rryy)?",
            options = listOf("9/16", "3/16", "1/16", "1/4"),
            correctOptionIndex = 2,
            solutionText = "Detailed AI Solution:\n1. This is a classic Mendelian dihybrid cross (RrYy x RrYy).\n2. According to the Law of Independent Assortment, the seed texture (Rr) and seed color (Yy) segregate independently.\n3. The probability of getting wrinkled seeds (rr) from Rr x Rr is 1/4.\n4. The probability of getting green seeds (yy) from Yy x Yy is 1/4.\n5. Since the two traits assort independently, we multiply their individual probabilities to get the combined probability for wrinkled green seeds (rryy):\n   P(rryy) = P(rr) * P(yy) = 1/4 * 1/4 = 1/16.\n\nThus, 1/16 of the total offspring are expected to be wrinkled green."
        ),
        Question(
            id = 6,
            subject = "Biology",
            topic = "Cellular Respiration",
            text = "During aerobic respiration, the final electron acceptor of the electron transport chain (ETC) in mitochondria is:",
            options = listOf("Oxygen (O2)", "Cytochrome c", "NAD+", "Water (H2O)"),
            correctOptionIndex = 0,
            solutionText = "Detailed AI Solution:\n1. In the inner mitochondrial membrane, the Electron Transport Chain (ETC) passes high-energy electrons through a series of complexes (Complex I to IV).\n2. This movement releases energy used to pump protons (H+) and establish a gradient.\n3. At Complex IV (Cytochrome c oxidase), the electrons are finally transferred. \n4. Molecular Oxygen (O2) acts as the terminal or final electron acceptor. It binds with free hydrogen protons (H+) to form Water (H2O) as a stable by-product:\n   O2 + 4H+ + 4e- -> 2H2O.\n5. Without Oxygen, the ETC backups, halting ATP synthesis via oxidative phosphorylation.\n\nHence, the correct answer is Oxygen (O2)."
        )
    )
}

// --- ROOM ENTITIES ---

// Local User Table
@Entity(tableName = "user_sessions")
data class UserSessionEntity(
    @PrimaryKey val phoneNumber: String,
    val name: String,
    val isPremium: Boolean,
    val currentOtp: String? = null,
    val lastLoginTimestamp: Long = System.currentTimeMillis()
)

// List of solved questions and status
@Entity(tableName = "question_attempts")
data class AttemptEntity(
    @PrimaryKey val questionId: Int,
    val selectedOptionIndex: Int,
    val isCorrect: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

// Chat message for doubt solving
@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val questionId: Int?, // Optional context: connected to specific question
    val role: String,     // "user" or "model"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
