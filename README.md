Shower Time for Android
Shower Time est une application Android native, accompagnée de son application pour montre connectée (Wear OS), conçue pour vous aider à optimiser votre temps sous la douche, à économiser l'eau et à suivre une routine d'hygiène structurée.

Concept
L'application agit comme un coach de douche personnel. Elle vous guide, étape par étape, à travers des programmes de douche prédéfinis ou personnalisés. Que vous utilisiez votre téléphone ou votre montre, vous recevez des alertes claires (sonores ou vibrantes) pour passer à l'étape suivante, vous permettant de rester concentré et efficace.

Fonctionnalités Principales
📱 Application Téléphone : L'application principale pour gérer vos programmes, consulter vos statistiques et utiliser le mode douche avec un guidage audio détaillé.

⌚ Application Montre (Wear OS) : Une extension légère pour lancer et suivre vos programmes directement depuis votre poignet, avec des retours haptiques (vibrations) discrets.

🔄 Synchronisation Transparente : Les programmes créés sur le téléphone sont instantanément disponibles sur la montre grâce à la Wearable Data Layer API.

🎵 Guidage Adapté :

Sur le téléphone, des sons distincts pour chaque phase vous guident sans que vous ayez besoin de regarder l'écran.

Sur la montre, des vibrations vous alertent à chaque transition.

⭐ Fonctionnalités Premium : Un achat unique débloque la possibilité de créer et de modifier vos propres programmes de douche de manière illimitée.

Stack Technique
Ce projet est développé en suivant les pratiques modernes d'Android.

Langage : 100% Kotlin

Architecture : Projet multi-modules (app et wear)

UI (Téléphone) : Android Views en XML, avec RecyclerView pour les listes.

UI (Montre) : Jetpack Compose for Wear OS, le framework déclaratif moderne.

Synchronisation : Wearable Data Layer API pour la communication entre le téléphone et la montre.

Paiements : Google Play Billing Library pour la gestion de l'achat premium.

Dépendances Notables : Gson pour la sérialisation des données.

Structure du Projet
/app : Contient le code source de l'application pour smartphone.

/wear : Contient le code source de l'application pour montre connectée (Wear OS).

Démarrage
Clonez ce dépôt : git clone [URL_DU_DEPOT]

Ouvrez le projet avec la dernière version stable d'Android Studio.

Configurez un émulateur de téléphone et un émulateur Wear OS (ou connectez des appareils physiques).

Lancez les cibles app et wear sur leurs appareils respectifs.
