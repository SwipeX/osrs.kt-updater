package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

fun staticMembers() = classIdentity {
    name = "N/A"
    memberIdentity {
        name = "players"
        desc = "[L{Player};"
    }
    memberIdentity {
        name = "npcs"
        desc = "[L{Npc};"
    }
    memberIdentity {
        name = "widgets"
        desc = "[[L{Widget};"
    }
    memberIdentity {
        name = "groundItems"
        desc = "[[[L{NodeDeque};"
    }
    memberIdentity {
        name = "exchangeItems"
        desc = "[L{ExchangeOffer};"
    }
    memberIdentity {
        name = "collisionMaps"
        desc = "[L{CollisionMap};"
    }
    memberIdentity {
        name = "region"
        desc = "L{Region};"
    }
}
