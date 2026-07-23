$version = '5.0.6'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'A9293364F3B6D863CCCE971B27041C2E40FB54AF81A9C6E7AF34AD3046A9E1EA'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
