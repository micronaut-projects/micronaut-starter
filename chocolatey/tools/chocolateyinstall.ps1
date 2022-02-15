$version = '3.3.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'F2292256B41F32AB23C1AF04814D53C33AF6856F38CEB740975A1762D2802662'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
