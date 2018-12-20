package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity
import kt.osrs.analysis.rank.usage.GETFIELD
import kt.osrs.analysis.rank.usage.PUTFIELD

class ExchangeOffer : Identifiable() {
    override val executeIndex = 39
    override val identity = classIdentity {
        name = "ExchangeOffer"
        staticDefinition {
            superName = "java/lang/Object"
            "B" occurs 1
            "I" occurs 5
        }

        /*
        ^* c.r(I) identified as getItemID
        ^* c.q(I) identified as getQuantity
         */
        memberIdentity {
            name = "quantity"
            desc = "I"
            usageDefinition {
                "{ExchangeOffer}" from "(L{NodeByteBuffer};BI)V" using PUTFIELD x 1
            }
        }
        memberIdentity {
            name = "itemID"
            desc = "I"
            usageDefinition {
                "{ExchangeOwner}" from "(L{NodeByteBuffer};Z)V" using PUTFIELD x 1
            }
        }

        memberIdentity {
            name = "state"
            desc = "B"
            usageDefinition {
                "{Player}" from "(L{NodeByteBuffer};)V" using GETFIELD x 1 and PUTFIELD x 1
            }
        }
    }
}