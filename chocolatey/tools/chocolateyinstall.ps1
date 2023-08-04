$version = '4.0.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '63738B85639175C55051A50494E0F0C0D0A9664845A0506B994B9C2D872C5D78'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
