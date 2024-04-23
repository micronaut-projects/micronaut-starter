$version = '4.4.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'C6B189C16170049D89098FE80C3FEBF783AF50C8443BEA1884C376CD0EE4CF9D'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
