import Link from "next/link";
import "./globals.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUser, faHome } from "@fortawesome/free-solid-svg-icons"; 

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang='en'>
      <body className='antialiased'>
        <header className='p-6 bg-[#19191f] text-white flex items-center justify-between shadow-md'>
          {/* Logo po lewej stronie */}
          <div className='flex items-center'>
            <img src='/images/logo.png' alt='Logo' className='h-12 w-12 mr-4' />
            <span className='text-2xl font-bold'>AutoDream</span>
          </div>

          {/* Nawigacja po prawej stronie */}
          <nav className='flex space-x-6'>
            <Link
              href='/'
              className='hover:text-gray-400 flex flex-col items-center'>
              <FontAwesomeIcon icon={faHome} className='h-6 w-6 mb-1' />{" "}
              {/* Ikona Home */}
              <span className='text-sm'>Strona główna</span>
            </Link>
            <Link
              href='/account'
              className='hover:text-gray-400 flex flex-col items-center'>
              <FontAwesomeIcon icon={faUser} className='h-6 w-6 mb-1' />
              <span className='text-sm'>Twoje konto</span>
            </Link>
          </nav>
        </header>
        {children}
      </body>
    </html>
  );
}
