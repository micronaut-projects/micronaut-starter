$version = '4.3.5'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '755841DA2C3D6FCA80D981C2C674C9A7031790DC3ECCE26DE28DB98657BF51BB'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
