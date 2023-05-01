$version = '3.9.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'DB56000634407E3D03566D2AAAC2F8D184A99716986F7137BFA56DFA4D17C9F5'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
